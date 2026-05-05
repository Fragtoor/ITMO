package main_classes;

import commands.*;
import commands.auth.Login;
import commands.auth.Logout;
import commands.auth.Register;
import commands.collection.*;
import commands.other.Exit;
import common.exceptions.InvalidInputException;
import common.exceptions.RecursiveCallException;
import common.net.Response;
import common.net.ResponseType;
import common.net.User;
import common.tools.Reader;
import common.tools.FileManager;
import reader_manager.InputManager;
import tools.*;
import common.ui.ConsoleColors;

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
            System.out.println(ConsoleColors.BLUE + "Добро пожаловать!\n" + ConsoleColors.RESET);
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
            if (!openedScripts.isEmpty()) {
                openedScripts.pop();
            }
        }

        CommandClient command = InputManager.getCommand();
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
        String fileName = command.getParams()[0];
        try {
            absolutePath = new File(fileName).getCanonicalPath();
        } catch (IOException e) {
            absolutePath = fileName;
        }

        try {
            if (!FileManager.fileExists(absolutePath)) {throw new FileNotFoundException(ConsoleColors.RED + "Укажите правильный путь к файлу!\n" + ConsoleColors.RESET);}
            if (!FileManager.hasRighToRead(absolutePath)) {throw new FileNotFoundException(ConsoleColors.RED + "Нет прав на чтение файла!\n" + ConsoleColors.RESET);}
            if (openedScripts.contains(absolutePath)) {
                throw new RecursiveCallException(ConsoleColors.RED + "Обнаружена рекурсия! Файл " + fileName + " уже выполняется.\n" + ConsoleColors.RESET);
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
                System.out.println(ConsoleColors.GREEN + "Вы вышли из аккаунта\n" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "Вы не авторизованы\n" + ConsoleColors.RESET);
            }
            return false;
        }

        if ((command instanceof Login || command instanceof Register) && currentUser != null && currentUser.isConfirm()) {
            System.out.println(ConsoleColors.BLUE + "Вы уже вошли\n" + ConsoleColors.RESET);
            return false;
        }

        if (currentUser == null && !(command instanceof Login) && !(command instanceof Register)) {
            System.out.println(ConsoleColors.BLUE + "Сначала авторизуйтесь, чтобы писать команды");
            System.out.println("- login : войти в аккаунт");
            System.out.println("- register : зарегистрироваться\n" + ConsoleColors.RESET);
            return false;
        }
        return true;
    }
}
