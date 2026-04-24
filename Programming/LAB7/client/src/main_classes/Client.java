package main_classes;

import commands.*;
import commands.auth.Login;
import commands.auth.Register;
import commands.collection.*;
import commands.other.Exit;
import common.exceptions.InvalidInputException;
import common.exceptions.RecursiveCallException;
import common.general.Response;
import common.tools.Reader;
import common.tools.FileManager;
import reader_manager.InputManager;
import tools.*;

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
    private InetAddress host;
    private AuthService authService;
    private int port;
    private SocketChannel socketChannel;
    private int connectionAttempts = 0;
    private final int MAX_RECONNECT_ATTEMPTS = 7;
    private final Stack<String> openedScripts = new Stack<>();
    private String currentUser = null; // null означает, что мы не авторизованы
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
        System.out.println("Максимальное количество попыток подключения истрачено. Выход.");
        System.exit(0);
    }

    private void handleDisconnect() {
        System.out.println("Потеряно соединение. Повтор...");
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
        System.out.println("Клиент запустился\n");
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
            System.out.println("Команды из файла исполнены!\n");
            InputManager.restoreConsoleInput();
            openedScripts.clear();
        }

        CommandClient command = InputManager.startInput();
        if (command == null) return;

        if (command instanceof Exit) {
            System.exit(0);
        }

        if (currentUser == null && !(command instanceof Login) && !(command instanceof Register)) {
            System.out.println("\nСначала авторизуйтесь, чтобы писать команды");
            System.out.println("- login : войти в аккаунт");
            System.out.println("- register : зарегистрироваться\n");
            return;
        }

        // ОБРАБОТКА EXECUTE_SCRIPT
        if (command instanceof ExecuteScript) {
            try {
                command.validate();
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
                return;
            }

            String absolutePath;
            String fileName = (String)command.getParameter();
            try {
                absolutePath = new File(fileName).getCanonicalPath();
            } catch (IOException e) {
                absolutePath = fileName;
            }

            if (!FileManager.fileExists(absolutePath)) {throw new FileNotFoundException("Укажите правильный путь к файлу!\n");}
            if (!FileManager.hasRighToRead(absolutePath)) {throw new FileNotFoundException("Нет прав на чтение файла!\n");}
            try {
                if (openedScripts.contains(absolutePath)) {
                    throw new RecursiveCallException("Обнаружена рекурсия! Файл " + fileName + " уже выполняется.\n");
                }

                openedScripts.add(absolutePath);
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(isr);

                InputManager.setFileInput(new Scanner(reader));

                // Селектор на следующем круге вызовет этот же метод, и startInput() начнет читать уже из файла.
                return;

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                openedScripts.remove(absolutePath);
                return;
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
                openedScripts.remove(absolutePath);
                return;
            }
        }

        // отправка обычной команды на сервер
        try {
            command.validate();
            command.setFromTheFile(InputManager.getReadingFromFile());
            NetWork.sendRequest(client, command.toRequest());
            key.interestOps(SelectionKey.OP_READ);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleReadOperation(SelectionKey key)
            throws IOException, ClassNotFoundException {
        Object result = Reader.reader(key);

        if (result == null) return;

        String answer = ((Response)result).getMessage();
        System.out.println(answer);
        key.interestOps(SelectionKey.OP_WRITE);
    }
}
