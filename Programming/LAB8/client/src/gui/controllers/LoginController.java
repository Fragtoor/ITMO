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
                showError("login.error.empty_fields");
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
                response -> {
                    User authenticatedUser = (User) response.getObj();
                    client.setCurrentUser(authenticatedUser);

                    if (authenticatedUser.getRole() != null && authenticatedUser.getRole().equalsIgnoreCase("ADMIN")) {
                        windowManager.showAdminMainWindow(authenticatedUser);
                    } else {
                        windowManager.showMainWindow(authenticatedUser);
                    }
                },
                errorMessage -> {
                    if (errorMessage.equals("NO_CONNECTION")) {
                        showError("login.error.connect_to_server");
                    } else {
                        showError(errorMessage);
                    }
                }
        );
    }

    private void setupI18n() {
        view.languageBox.getItems().addAll("RU / Русский", "NL / Nederlands", "SV / Svenska", "EN / English");
        view.languageBox.getSelectionModel().select(0);
        String savedLang = windowManager.getCurrentLanguage();

        view.languageBox.setValue(savedLang);

        changeLanguage(savedLang);
    }

    private void changeLanguage(String langSelection) {
        windowManager.setCurrentLanguage(langSelection);

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

    private void showError(String msg) {
        if (bundle != null && bundle.containsKey(msg)) {
            this.currentErrorKey = msg;
            view.errorLabel.setText(bundle.getString(msg));
        } else {
            this.currentErrorKey = null;
            view.errorLabel.setText(msg);
        }
        view.errorLabel.setVisible(true);
    }
}