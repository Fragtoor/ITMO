package gui.controllers;

import commands.auth.Login;
import common.net.User;
import gui.views.LoginView;
import javafx.stage.Stage;
import main_classes.WindowManager;
import net.Client;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {
    final LoginView view;
    final Client client;
    final WindowManager windowManager;
    private ResourceBundle bundle;
    private String currentErrorKey = null;
    private final Stage currentStage;

    public LoginController(LoginView view, Client client, WindowManager windowManager, Stage stage) {
        this.view = view;
        this.client = client;
        this.windowManager = windowManager;
        this.currentStage = stage;

        setupI18n();
        setupEvents();
    }

    private void setupEvents() {
        view.languageBox.setOnAction(e -> changeLanguage(view.languageBox.getValue()));

        view.loginButton.setOnAction(event -> {
            String login = view.loginField.getText();
            String password = view.passwordField.getText();

            if (login.isBlank() || password.isBlank()) {
                showLocalizedError("login.error.empty_fields");
                return;
            }

            handleLoginProcess(login, password);
        });

        view.createAccountLink.setOnAction(event -> windowManager.showRegisterWindow());
    }

    private void handleLoginProcess(String login, String password) {
        if (client == null) return;

        User user = new User(login, password);
        view.errorLabel.setVisible(false);

        Login loginCommand = new Login();
        client.setCurrentUser(user);

        client.sendCommandAsync(loginCommand,
                // Блок onSuccess
                response -> {
                    client.setCurrentUser(loginCommand.getUser());
                    windowManager.showMainWindow(user);
                },
                // Блок onError
                errorMessage -> {
                    if (errorMessage.equals("NO_CONNECTION")) {
                        showLocalizedError("login.error.connect_to_server"); // Перевод ошибки
                    } else {
                        showError(errorMessage); // Обычный текст
                    }
                }
        );
    }

    private void setupI18n() {
        view.languageBox.getItems().addAll("RU / Русский", "NL / Nederlands", "SV / Svenska", "EN / English");
        view.languageBox.getSelectionModel().select(0);
        changeLanguage("RU / Русский");
    }

    private void changeLanguage(String langSelection) {
        Locale locale = switch (langSelection) {
            case "NL / Nederlands" -> new Locale("nl", "NL");
            case "SV / Svenska" -> new Locale("sv", "SE");
            case "EN / English" -> new Locale("en", "AU");
            default -> new Locale("ru", "RU");
        };
        bundle = ResourceBundle.getBundle("resources.properties.messages", locale);
        updateTexts();
    }

    private void updateTexts() {
        view.loginField.setPromptText(bundle.getString("login.login"));
        view.passwordField.setPromptText(bundle.getString("login.password"));
        view.loginButton.setText(bundle.getString("login.button"));
        view.createAccountLink.setText(bundle.getString("login.register"));

        if (view.errorLabel.isVisible() && currentErrorKey != null) {
            view.errorLabel.setText(bundle.getString(currentErrorKey));
        }

        currentStage.setTitle(bundle.getString("login.title"));
    }

    private void showLocalizedError(String key) {
        this.currentErrorKey = key;
        view.errorLabel.setText(bundle.getString(key));
        view.errorLabel.setVisible(true);
    }

    private void showError(String msg) {
        this.currentErrorKey = null;
        view.errorLabel.setText(msg);
        view.errorLabel.setVisible(true);
    }
}