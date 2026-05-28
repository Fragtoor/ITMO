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
import java.util.ResourceBundle;

public class ScriptExecutor {
    private final Stack<String> openedScripts = new Stack<>();
    private final Client client;
    private ResourceBundle bundle;

    public ScriptExecutor(Client client) {
        this.client = client;
    }

    public void run(String fileName, Consumer<String> logger, Runnable onComplete) {
        String absolutePath;
        try {
            absolutePath = new File(fileName).getCanonicalPath();
        } catch (IOException e) {
            absolutePath = fileName;
        }
        try {
            if (!FileManager.fileExists(absolutePath)) {
                Platform.runLater(() -> logger.accept(bundle != null ? bundle.getString("script.error.not_found") : "[ОШИБКА]: Файл скрипта не найден!"));
                return;
            }
            if (!FileManager.hasRighToRead(absolutePath)) {
                Platform.runLater(() -> logger.accept(bundle != null ? bundle.getString("script.error.no_rights") : "[ОШИБКА]: Нет прав на чтение файла!"));
                return;
            }
            if (openedScripts.contains(absolutePath)) {
                Platform.runLater(() -> logger.accept(bundle != null ? bundle.getString("script.error.recursion") : "[ОШИБКА]: Обнаружена рекурсия!"));
                return;
            }

            openedScripts.add(absolutePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(absolutePath), StandardCharsets.UTF_8));
            InputManager.setFileInput(new Scanner(reader));

            execute(logger, onComplete);
        } catch (Exception e) {
            Platform.runLater(() -> logger.accept((bundle != null ? bundle.getString("script.error.read") : "[ОШИБКА]: Ошибка чтения") + ": " + e.getMessage()));
            openedScripts.remove(absolutePath);
        }
    }

    private void execute(Consumer<String> logger, Runnable onComplete) {
        CommandClient command;

        try {
            command = InputManager.getCommand();
        } catch (Exception e) {
            logError(logger, e.getMessage());
            command = new CommandClient(null);
        }

        while (command != null) {
            try {
                command.validate();
            } catch (Exception e) {
                logError(logger, e.getMessage());
                try {
                    command = InputManager.getCommand();
                } catch (Exception ex) {
                    logError(logger, ex.getMessage());
                    command = new CommandClient(null);
                }
                continue;
            }

            if (command instanceof ExecuteScript) {
                if (command.getParams() != null && command.getParams().length > 0) {
                    run(command.getParams()[0], logger, onComplete);
                }
                try {
                    command = InputManager.getCommand();
                } catch (Exception ex) {
                    logError(logger, ex.getMessage());
                    command = new CommandClient(null);
                }
                continue;
            }

            try {
                command.prepareData();
            } catch (Exception e) {
                logError(logger, e.getMessage());
                try {
                    command = InputManager.getCommand();
                } catch (Exception ex) {
                    logError(logger, ex.getMessage());
                    command = new CommandClient(null);
                }
                continue;
            }

            if (command.getParams() != null) {
                try { Thread.sleep(20); } catch (InterruptedException ignored) {}
                client.sendCommandAsync(command,
                        response -> {},
                        errorMessage -> logError(logger, errorMessage)
                );
            }

            try {
                command = InputManager.getCommand();
            } catch (Exception e) {
                logError(logger, e.getMessage());
                command = new CommandClient(null);
            }
        }

        InputManager.restoreConsoleInput();
        if (!openedScripts.isEmpty()) {
            openedScripts.pop();
        }

        Platform.runLater(() -> {
            logger.accept(bundle != null ? bundle.getString("script.info.done") : "[ИНФО]: DONE");
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    private void logError(Consumer<String> logger, String message) {
        Platform.runLater(() -> {
            String prefix = (bundle != null && bundle.containsKey("script.error.command"))
                    ? bundle.getString("script.error.command")
                    : "[ОШИБКА скрипта]";

            String cleanMessage = message != null ? message.replaceAll("\u001B\\[[;\\d]*m", "") : "Неизвестная ошибка";

            logger.accept(prefix + ": " + cleanMessage);
        });
    }
}