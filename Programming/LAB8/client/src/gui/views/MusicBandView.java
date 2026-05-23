package gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MusicBandView extends VBox {

    public final ComboBox<String> languageBox = new ComboBox<>();

    // Поля ввода
    public final TextField nameField = new TextField();
    public final TextField participantsField = new TextField();
    public final TextField albumsField = new TextField();
    public final TextField salesField = new TextField();

    public final Label coordLabel = new Label();
    public final TextField coordX = new TextField();
    public final TextField coordY = new TextField();

    public final Label genreLabel = new Label();
    public final ComboBox<String> genreBox = new ComboBox<>();

    public final Label dateLabel = new Label();
    public final DatePicker datePicker = new DatePicker();

    public final Button cancelButton = new Button();
    public final Button saveButton = new Button();

    // Звездочки - обязательные элементы
    public final Label starName = makeStar();
    public final Label starParticipants = makeStar();
    public final Label starAlbum = makeEmptyStar();
    public final Label starSales = makeStar();
    public final Label starGenre = makeStar();
    public final Label starDate = makeStar();

    public final Label errorLabel = new Label();

    public MusicBandView() {
        this.setSpacing(15);
        this.setPadding(new Insets(20, 30, 25, 30));
        this.setAlignment(Pos.CENTER);

        this.setStyle("-fx-background-color: #2A363F; " +
                "-fx-border-color: #4a5a6a; " +
                "-fx-border-width: 2px;");

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.getChildren().addAll(languageBox);

        VBox fieldsBox = new VBox(12);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setMaxWidth(350);

        HBox xyBox = new HBox(15);
        xyBox.setAlignment(Pos.CENTER_LEFT);

        HBox xRow = new HBox(8, makeEmptyStar(), coordX);
        HBox.setHgrow(coordX, Priority.ALWAYS);

        HBox yRow = new HBox(8, makeStar(), coordY);
        HBox.setHgrow(coordY, Priority.ALWAYS);

        xyBox.getChildren().addAll(xRow, yRow);
        VBox coordContainer = new VBox(2, coordLabel, xyBox);
        coordContainer.setAlignment(Pos.CENTER_LEFT);

        VBox genreContainer = new VBox(2, genreLabel, buildFieldRow(starGenre, genreBox));
        genreContainer.setAlignment(Pos.CENTER_LEFT);

        VBox dateContainer = new VBox(2, dateLabel, buildFieldRow(starDate, datePicker));
        dateContainer.setAlignment(Pos.CENTER_LEFT);

        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(cancelButton, saveButton);

        fieldsBox.getChildren().addAll(
                buildFieldRow(starName, nameField),
                buildFieldRow(starParticipants, participantsField),
                buildFieldRow(starAlbum, albumsField),
                buildFieldRow(starSales, salesField),
                coordContainer,
                genreContainer,
                dateContainer,
                new Region(),
                errorLabel,
                buttonsBox
        );

        this.getChildren().addAll(topBar, fieldsBox);

        applyStyles();
    }

    // Склейка звездочки и поля в одну строку
    private HBox buildFieldRow(Label star, Control field) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(field, Priority.ALWAYS);
        field.setMaxWidth(Double.MAX_VALUE);
        row.getChildren().addAll(star, field);
        return row;
    }

    private void applyStyles() {
        languageBox.setPrefWidth(130);
        languageBox.setStyle("-fx-background-color: #808080; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 4;" +
                "-fx-font-family: 'Verdana'; -fx-font-size: 11px;"
        );

        String fieldStyle = "-fx-background-color: #2A363F;" +
                " -fx-text-fill: #808080;" +
                " -fx-background-radius: 4;" +
                " -fx-border-color: #808080;" +
                " -fx-border-radius: 4;";
        Font fieldFont = Font.font("Verdana", 13);

        TextField[] textFields = {nameField, participantsField, albumsField, salesField, coordX, coordY};
        for (TextField tf : textFields) {
            tf.setPrefHeight(35);
            tf.setFont(fieldFont);
            tf.setStyle(fieldStyle);
        }
        String fieldStyle2 = "-fx-background-color: #808080;" +
                " -fx-text-fill: white;" +
                " -fx-background-radius: 4;" +
                " -fx-border-color: #808080;" +
                " -fx-border-radius: 4;";

        genreBox.setPrefHeight(35);
        genreBox.setMaxWidth(Double.MAX_VALUE);
        genreBox.setStyle(fieldStyle2 + " -fx-font-family: 'Verdana'; -fx-font-size: 13px;");
        genreBox.getItems().addAll("JAZZ", "PUNK_ROCK", "POST_ROCK", "MATH_ROCK", "BLUES");
        genreBox.setValue("JAZZ");

        datePicker.setPrefHeight(35);
        datePicker.setMaxWidth(Double.MAX_VALUE);
        datePicker.setStyle(fieldStyle);
        datePicker.getEditor().setStyle("-fx-background-color: #2A363F; -fx-text-fill: white; -fx-font-family: 'Verdana'; -fx-font-size: 13px;");

        Font labelFont = Font.font("Verdana", 12);
        String labelStyle = "-fx-text-fill: white; -fx-padding: 0 0 0 18;";

        coordLabel.setFont(labelFont); coordLabel.setStyle(labelStyle);
        genreLabel.setFont(labelFont); genreLabel.setStyle(labelStyle);
        dateLabel.setFont(labelFont); dateLabel.setStyle(labelStyle);

        cancelButton.setPrefSize(130, 35);
        cancelButton.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        cancelButton.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;");

        saveButton.setPrefSize(130, 35);
        saveButton.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        saveButton.setStyle("-fx-background-color: #24af6f; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;");

        errorLabel.setFont(Font.font("Verdana", 12));
        errorLabel.setStyle("-fx-text-fill: #ff6b6b;");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private static Label makeStar() {
        Label star = new Label("*");
        star.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        star.setStyle("-fx-text-fill: #ff6b6b;");
        star.setMinWidth(10);
        return star;
    }

    private static Label makeEmptyStar() {
        Label star = new Label("*");
        star.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        star.setStyle("-fx-text-fill: transparent;");
        star.setMinWidth(10);
        return star;
    }
}