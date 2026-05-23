package gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RegisterView extends VBox {
    public final ComboBox<String> languageBox = new ComboBox<>();
    public final TextField nameField = new TextField();
    public final TextField loginField = new TextField();

    public final PasswordField passwordField = new PasswordField();

    public final Label reqLength = new Label();
    public final Label reqDigit = new Label();
    public final Label reqLower = new Label();
    public final Label reqUpper = new Label();
    public final VBox passwordRequirementsBox = new VBox(2);

    public final PasswordField passwordRepeatField = new PasswordField();

    public final Label errorLabel = new Label();
    public final Button registerButton = new Button();

    public RegisterView() {
        this.setSpacing(25);
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

        passwordRequirementsBox.getChildren().addAll(reqLength, reqDigit, reqLower, reqUpper);
        passwordRequirementsBox.setPadding(new Insets(0, 0, 0, 5));

        VBox passGroup = new VBox(3, passwordField, passwordRequirementsBox);

        VBox fieldsBox = new VBox(20);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setMaxWidth(350);

        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(registerButton);

        fieldsBox.getChildren().addAll(
                nameField,
                loginField,
                passGroup,
                passwordRepeatField,
                errorLabel,
                buttonsBox
        );

        this.getChildren().addAll(topBar, fieldsBox);

        applyStyles();
    }

    private void applyStyles() {
        languageBox.setPrefWidth(140);
        languageBox.setStyle("-fx-background-color: #808080; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-family: 'Verdana';");

        String fieldStyle = "-fx-background-color: #2A363F; -fx-text-fill: #808080; -fx-background-radius: 5; -fx-border-color: #808080; -fx-border-radius: 5;";
        Font fieldFont = Font.font("Verdana", 15);

        nameField.setPrefHeight(45);
        nameField.setFont(fieldFont);
        nameField.setStyle(fieldStyle);

        loginField.setPrefHeight(45);
        loginField.setFont(fieldFont);
        loginField.setStyle(fieldStyle);

        passwordField.setPrefHeight(45);
        passwordField.setFont(fieldFont);
        passwordField.setStyle(fieldStyle);

        passwordRepeatField.setPrefHeight(45);
        passwordRepeatField.setFont(fieldFont);
        passwordRepeatField.setStyle(fieldStyle);

        errorLabel.setFont(Font.font("Verdana", 13));
        errorLabel.setStyle("-fx-text-fill: #ff6b6b;");
        errorLabel.setMinHeight(16.0);
        errorLabel.setVisible(false);

        String reqStyle = "-fx-text-fill: #95a5a6; -fx-font-size: 11px; -fx-font-family: 'Verdana';";
        reqLength.setStyle(reqStyle);
        reqDigit.setStyle(reqStyle);
        reqLower.setStyle(reqStyle);
        reqUpper.setStyle(reqStyle);

        registerButton.setPrefSize(180, 45);
        registerButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        registerButton.setStyle("-fx-background-color: #24af6f; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
    }
}