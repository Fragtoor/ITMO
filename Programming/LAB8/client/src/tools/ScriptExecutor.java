package tools;

import commands.CommandClient;
import commands.collection.ExecuteScript;
import common.tools.FileManager;
import net.Client;
import reader_manager.InputManager;
import javafx.application.Platform;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.Consumer;

public class ScriptExecutor {
    private final Stack<String> openedScripts = new Stack<>();
    private final Client client;

    public ScriptExecutor(Client client) {
        this.client = client;
    }

    public void run(String fileName, Consumer<String> logger) {
        String absolutePath;
        try {
            absolutePath = new File(fileName).getCanonicalPath();
        } catch (IOException e) {
            absolutePath = fileName;

        }
        try {
            if (!FileManager.fileExists(absolutePath)) {
                Platform.runLater(() -> logger.accept("[ОШИБКА]: Файл скрипта не найден!"));
                return;
            }
            if (!FileManager.hasRighToRead(absolutePath)) {
                Platform.runLater(() -> logger.accept("[ОШИБКА]: Нет прав на чтение файла!"));
                return;
            }
            if (openedScripts.contains(absolutePath)) {
                Platform.runLater(() -> logger.accept("[ОШИБКА]: Обнаружена рекурсия!"));
                return;
            }

            openedScripts.add(absolutePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(absolutePath), StandardCharsets.UTF_8));
            InputManager.setFileInput(new Scanner(reader));

            execute(logger);

        } catch (Exception e) {
            Platform.runLater(() -> logger.accept("[ОШИБКА]: Ошибка чтения: " + e.getMessage()));
            openedScripts.remove(absolutePath);
        }
    }

    private void execute(Consumer<String> logger) {
        CommandClient command;
        try { command = InputManager.getCommand(); } catch (Exception e) { command = new CommandClient(null); }

        while (command != null) {
            try {
                command.validate();
            } catch (Exception e) {
                try { command = InputManager.getCommand(); } catch (Exception ex) { command = new CommandClient(null); }
                continue;
            }

            if (command instanceof ExecuteScript) {
                if (command.getParams() != null && command.getParams().length > 0) {
                    run(command.getParams()[0], logger);
                }
                try { command = InputManager.getCommand(); } catch (Exception ex) { command = new CommandClient(null); }
                continue;
            }

            try {
                command.prepareData();
            } catch (Exception e) {
                try { command = InputManager.getCommand(); } catch (Exception ex) { command = new CommandClient(null); }
                continue;
            }

            // Отправка команды
            if (command.getParams() != null) {
                try { Thread.sleep(20); } catch (InterruptedException ignored) {}
                client.sendCommandAsync(command,
                        response -> {},
                        errorMessage -> Platform.runLater(() -> logger.accept("[ОШИБКА скрипта]: " + errorMessage))
                );
            }

            try {
                command = InputManager.getCommand();
            } catch (Exception e) {
                command = new CommandClient(null);
            }
        }

        InputManager.restoreConsoleInput();
        if (!openedScripts.isEmpty()) {
            openedScripts.pop();
        }

        Platform.runLater(() -> logger.accept("[ИНФО]: Скрипт выполнен."));
    }
}