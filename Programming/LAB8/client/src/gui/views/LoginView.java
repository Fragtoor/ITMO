package gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginView extends VBox {
    public final ComboBox<String> languageBox = new ComboBox<>();
    public final TextField loginField = new TextField();
    public final PasswordField passwordField = new PasswordField();
    public final Label errorLabel = new Label();
    public final Button loginButton = new Button();
    public final Hyperlink createAccountLink = new Hyperlink();

    public LoginView() {
        this.setSpacing(30);
        this.setPadding(new Insets(40));
        this.setAlignment(Pos.CENTER);

        this.setStyle("-fx-background-color: #2A363F; " +
                "-fx-border-color: #4a5a6a; " +
                "-fx-border-width: 2px;");

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(spacer, languageBox);

        VBox fieldsBox = new VBox(20);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setMaxWidth(300);

        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER_LEFT);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        buttonsBox.getChildren().addAll(loginButton, spacer2, createAccountLink);

        fieldsBox.getChildren().addAll(loginField, passwordField, errorLabel, buttonsBox);

        this.getChildren().addAll(topBar, fieldsBox);

        applyStyles();
    }

    private void applyStyles() {
        languageBox.setPrefWidth(140);
        languageBox.setTranslateY(-40);
        languageBox.setStyle("-fx-background-color: #808080; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5;" +
                "-fx-font-family: 'Verdana';"
        );

        loginField.setPrefHeight(45);
        loginField.setFont(Font.font("Verdana", 15));
        loginField.setStyle("-fx-background-color: #2A363F;" +
                " -fx-text-fill: #808080;" +
                " -fx-background-radius: 5;" +
                " -fx-border-color: #808080; " +
                " -fx-border-radius: 5;");

        passwordField.setPrefHeight(45);
        passwordField.setFont(Font.font("Verdana", 15));
        passwordField.setStyle("-fx-background-color: #2A363F;" +
                " -fx-text-fill: #808080;" +
                " -fx-background-radius: 5;" +
                " -fx-border-color: #808080;" +
                " -fx-border-radius: 5;");

        errorLabel.setFont(Font.font("Verdana", 13));
        errorLabel.setStyle("-fx-text-fill: #ff6b6b;");
        errorLabel.setVisible(false);

        loginButton.setPrefSize(130, 45);
        loginButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        loginButton.setStyle("-fx-background-color: #24af6f; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");

        createAccountLink.setFont(Font.font("Verdana", 14));
        createAccountLink.setStyle("-fx-text-fill: #808080; -fx-border-color: transparent;");
    }

}