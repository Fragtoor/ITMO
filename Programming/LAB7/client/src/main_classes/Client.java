package main_classes;

import commands.*;
import commands.auth.Login;
import commands.auth.Logout;
import commands.auth.Register;
import commands.collection.*;
import commands.other.Exit;
import common.exceptions.InvalidInputException;
import common.exceptions.RecursiveCallException;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import common.tools.Reader;
import common.tools.FileManager;
import reader_manager.InputManager;
import tools.*;
import ui.ConsoleColors;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;


public class Client {
    private final InetAddress host;
    private AuthService authService;
    private final int port;
    private SocketChannel socketChannel;
    private int connectionAttempts = 0;
    private final int MAX_RECONNECT_ATTEMPTS = 7;
    private final Stack<String> openedScripts = new Stack<>();
    private User currentUser;
    private boolean isRunning = true;

    public Client(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception{
        while (connectionAttempts < MAX_RECONNECT_ATTEMPTS) {
            try (SocketChannel client = connectToServer()) {
                workWithServer(client);
            } catch (IOException | ClassNotFoundException e) {
                handleDisconnect();
            }
        }
        System.out.println(ConsoleColors.RED + "Максимальное количество попыток подключения истрачено. Выход." + ConsoleColors.RESET);
        System.exit(0);
    }

    private void handleDisconnect() {
        System.out.println(ConsoleColors.RED + "Потеряно соединение. Повтор..." + ConsoleColors.RESET);
        connectionAttempts++;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private SocketChannel connectToServer() throws IOException, InterruptedException {
        SocketChannel client = SocketChannel.open();
        client.configureBlocking(false);

        client.connect(new InetSocketAddress(host, port));

        while (!client.finishConnect()) {
            Thread.sleep(100);
        }

        connectionAttempts = 0;
        if (isRunning) {
            System.out.println("Добро пожаловать!\n");
            isRunning = false;
        }
        System.out.println(ConsoleColors.GREEN + "Клиент запустился\n" + ConsoleColors.RESET);
        return client;
    }

    private void workWithServer(SocketChannel client) throws IOException, ClassNotFoundException {
        try (Selector selector = Selector.open()) {
            client.register(selector, SelectionKey.OP_WRITE);

            while (client.isConnected()) {
                selector.select(2000);
                if (selector.selectedKeys().isEmpty()) continue;
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    // Клиент хочет написать серверу
                    if (key.isWritable()) {
                        handleWriteOperation(key);
                    }
                    // Клиент хочет получить ответ от сервера
                    if (key.isReadable()) {
                        handleReadOperation(key);
                    }
                }
            }
        }
    }

    private void handleWriteOperation(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();

        if (InputManager.isEndOfFile()) {
            System.out.println(ConsoleColors.BLUE + "Команды из файла прочитаны!\n" + ConsoleColors.RESET);
            InputManager.restoreConsoleInput();
            openedScripts.clear();
        }

        CommandClient command = InputManager.startInput();
        if (command == null) return;
        command.setUser(currentUser);

        if (command instanceof Exit) {
            System.exit(0);
        }

        // Проверка авторизации пользователя
        if (!processingAuth(command)) return;

        // Проверка на валидность введенных параметров
        try {
            command.validate();
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            return;
        }

        // ОБРАБОТКА EXECUTE_SCRIPT
        if (command instanceof ExecuteScript) {
            processingExecuteScript(command);
            return;
        }

        // отправка обычной команды на сервер
        command.setFromTheFile(InputManager.getReadingFromFile());
        var request = command.toRequest();
        if (command instanceof Login || command instanceof Register) currentUser = request.getUser();
        NetWork.sendRequest(client, request);
        key.interestOps(SelectionKey.OP_READ);
    }

    private void handleReadOperation(SelectionKey key)
            throws IOException, ClassNotFoundException {
        Object result = Reader.reader(key);

        if (result == null) return;

        Response response = ((Response)result);
        String message = response.getMessage();
        String details = response.getDetails();
        ResponseType responseType = response.getType();
        switch (responseType) {
            case COMMAND_SUCCESS:
                printResponse(ConsoleColors.GREEN + message, ConsoleColors.BLUE + details);
                break;
            case COMMAND_ERROR:
                printResponse(ConsoleColors.RED + message, ConsoleColors.BLUE + details);
                break;
            case AUTH_SUCCESS:
                printResponse(ConsoleColors.GREEN + message, ConsoleColors.BLUE + details);
                currentUser.setConfirm(true);
                break;
            case AUTH_ERROR:
                printResponse(ConsoleColors.RED + message, ConsoleColors.BLUE + details);
                currentUser = null;
                break;
            case SERVER_ERROR:
                printResponse(ConsoleColors.YELLOW + message, ConsoleColors.BLUE + details);
                break;
        }
        System.out.println();
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void printResponse(String message, String details) {
        if (!message.isBlank()) {
            System.out.println(message + ConsoleColors.RESET);
        }
        if (!details.isBlank()) {
            System.out.println(details + ConsoleColors.RESET);
        }
    }

    private void processingExecuteScript(CommandClient command) {
        String absolutePath;
        String fileName = (String)command.getParameter();
        try {
            absolutePath = new File(fileName).getCanonicalPath();
        } catch (IOException e) {
            absolutePath = fileName;
        }

        try {
            if (!FileManager.fileExists(absolutePath)) {throw new FileNotFoundException("Укажите правильный путь к файлу!\n");}
            if (!FileManager.hasRighToRead(absolutePath)) {throw new FileNotFoundException("Нет прав на чтение файла!\n");}
            if (openedScripts.contains(absolutePath)) {
                throw new RecursiveCallException("Обнаружена рекурсия! Файл " + fileName + " уже выполняется.\n");
            }

            openedScripts.add(absolutePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
            InputManager.setFileInput(new Scanner(reader));

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            openedScripts.remove(absolutePath);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Произошла ошибка: " + e.getMessage() + ConsoleColors.RESET);
            openedScripts.remove(absolutePath);
        }
    }

    private boolean processingAuth(CommandClient command) {
        if (command instanceof Logout) {
            if (currentUser != null && currentUser.isConfirm()) {
                currentUser = null;
                System.out.println("Вы вышли из аккаунта\n");
            } else {
                System.out.println("Вы не авторизованы\n");
            }
            return false;
        }

        if ((command instanceof Login || command instanceof Register) && currentUser != null && currentUser.isConfirm()) {
            System.out.println("Вы уже вошли\n");
            return false;
        }

        if (currentUser == null && !(command instanceof Login) && !(command instanceof Register)) {
            System.out.println("Сначала авторизуйтесь, чтобы писать команды");
            System.out.println("- login : войти в аккаунт");
            System.out.println("- register : зарегистрироваться\n");
            return false;
        }
        return true;
    }
}
