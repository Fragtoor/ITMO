package gui.controllers;

import commands.auth.Register;
import common.net.User;
import gui.views.RegisterView;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main_classes.WindowManager;
import net.Client;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegisterController {
    final RegisterView view;
    final Client client;
    final WindowManager windowManager;
    private ResourceBundle bundle;
    private final HashMap<Label, String> requirementMap = new HashMap<>();
    private final Stage currentStage;

    public RegisterController(RegisterView view, Client client, WindowManager windowManager, Stage stage) {
        this.view = view;
        this.client = client;
        this.windowManager = windowManager;
        this.currentStage = stage;

        requirementMap.put(view.reqLength, "❌");
        requirementMap.put(view.reqDigit, "❌");
        requirementMap.put(view.reqLower, "❌");
        requirementMap.put(view.reqUpper, "❌");

        setupI18n();
        setupEvents();
    }

    private void setupEvents() {
        applyRequirementState(view.reqLength, "register.reqLength");
        applyRequirementState(view.reqDigit, "register.reqDigit");
        applyRequirementState(view.reqLower, "register.reqLower");
        applyRequirementState(view.reqUpper, "register.reqUpper");

        view.languageBox.setOnAction(e -> changeLanguage(view.languageBox.getValue()));

        view.passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 8) {
                requirementMap.put(view.reqLength, "✅");
            } else {
                requirementMap.put(view.reqLength, "❌");
            }

            if (newValue.matches(".*\\d.*")) {
                requirementMap.put(view.reqDigit, "✅");
            } else {
                requirementMap.put(view.reqDigit, "❌");
            }

            if (newValue.matches(".*\\p{Ll}.*")) {
                requirementMap.put(view.reqLower, "✅");
            } else {
                requirementMap.put(view.reqLower, "❌");
            }

            if (newValue.matches(".*\\p{Lu}.*")) {
                requirementMap.put(view.reqUpper, "✅");
            } else {
                requirementMap.put(view.reqUpper, "❌");
            }
            updateTexts();
        });

        view.registerButton.setOnAction(event -> {
            String name = view.nameField.getText();
            String login = view.loginField.getText();
            String password = view.passwordField.getText();
            String passwordRepeat = view.passwordRepeatField.getText();

            if (login.isBlank() || password.isBlank() || name.isBlank() || passwordRepeat.isBlank()) {
                showError(bundle.getString("register.error.empty_fields"));
                return;
            }

            if (!view.reqLength.getText().contains("✅") || !view.reqDigit.getText().contains("✅")
                    || !view.reqLower.getText().contains("✅")
                    || !view.reqUpper.getText().contains("✅")) {
                showError(bundle.getString("register.error.not_constraint_passw"));
                return;
            }

            if (!view.passwordField.getText().equals(view.passwordRepeatField.getText())) {
                showError(bundle.getString("register.error.not_equals_passw"));
                return;
            }

            handleLoginProcess(name, login, password);
        });
    }

    private void handleLoginProcess(String name, String login, String password) {
        if (client == null) return;

        User user = new User(name, login, password);
        view.errorLabel.setVisible(false);

        Register registerCommand = new Register();
        client.setCurrentUser(user);

        client.sendCommandAsync(registerCommand,
                // Блок onSuccess
                response -> {
                    client.setCurrentUser(registerCommand.getUser());
                    windowManager.showMainWindow(user);
                },
                // Блок onError
                errorMessage -> {
                    if (errorMessage.equals("NO_CONNECTION")) {
                        showLocalizedError(view.errorLabel);
                    } else {
                        showError(errorMessage);
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
        view.nameField.setPromptText(bundle.getString("register.name"));
        view.loginField.setPromptText(bundle.getString("register.login"));
        view.passwordField.setPromptText(bundle.getString("register.password"));
        view.passwordRepeatField.setPromptText(bundle.getString("register.password_repeat"));
        view.registerButton.setText(bundle.getString("register.button"));

        applyRequirementState(view.reqLength, "register.reqLength");
        applyRequirementState(view.reqDigit, "register.reqDigit");
        applyRequirementState(view.reqLower, "register.reqLower");
        applyRequirementState(view.reqUpper, "register.reqUpper");

        currentStage.setTitle(bundle.getString("register.title"));
    }

    private void applyRequirementState(Label label, String bundleKey) {
        String icon = requirementMap.get(label);
        String translatedText = bundle.getString(bundleKey).trim();
        String fullText = icon + " " + translatedText;
        if (icon.equals("✅")) {
            setRequirementMet(label, fullText);
        } else {
            setRequirementUnmet(label, fullText);
        }
    }

    private void setRequirementMet(Label label, String text) {
        label.setText(text);
        label.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 11px; -fx-font-family: 'Verdana';");
    }

    private void setRequirementUnmet(Label label, String text) {
        label.setText(text);
        label.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 11px; -fx-font-family: 'Verdana';");
    }

    private void showLocalizedError(Label error) {
        error.setText(bundle.getString("login.error.connect_to_server"));
        error.setVisible(true);
    }

    private void showError(String msg) {
        view.errorLabel.setText(msg);
        view.errorLabel.setVisible(true);
    }
}