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
    public final Label nameLabel = new Label();
    public final TextField nameField = new TextField();

    public final Label participantsLabel = new Label();
    public final TextField participantsField = new TextField();

    public final Label albumsLabel = new Label();
    public final TextField albumsField = new TextField();

    public final Label salesLabel = new Label();
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
        fieldsBox.setAlignment(Pos.CENTER_LEFT);
        fieldsBox.setMaxWidth(350);

        // 1. Название
        VBox nameVBox = new VBox(2, nameLabel, nameField);
        nameVBox.setAlignment(Pos.CENTER_LEFT);
        nameField.setMaxWidth(Double.MAX_VALUE);
        HBox nameContainer = new HBox(8, starName, nameVBox);
        nameContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(nameVBox, Priority.ALWAYS);

        // 2. Участники
        VBox participantsVBox = new VBox(2, participantsLabel, participantsField);
        participantsVBox.setAlignment(Pos.CENTER_LEFT);
        participantsField.setMaxWidth(Double.MAX_VALUE);
        HBox participantsContainer = new HBox(8, starParticipants, participantsVBox);
        participantsContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(participantsVBox, Priority.ALWAYS);

        // 3. Альбомы
        VBox albumsVBox = new VBox(2, albumsLabel, albumsField);
        albumsVBox.setAlignment(Pos.CENTER_LEFT);
        albumsField.setMaxWidth(Double.MAX_VALUE);
        HBox albumsContainer = new HBox(8, starAlbum, albumsVBox);
        albumsContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(albumsVBox, Priority.ALWAYS);

        // 4. Сборы
        VBox salesVBox = new VBox(2, salesLabel, salesField);
        salesVBox.setAlignment(Pos.CENTER_LEFT);
        salesField.setMaxWidth(Double.MAX_VALUE);
        HBox salesContainer = new HBox(8, starSales, salesVBox);
        salesContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(salesVBox, Priority.ALWAYS);

        // 5. Координаты (X и Y)
        HBox xyBox = new HBox(15);
        xyBox.setAlignment(Pos.CENTER_LEFT);

        coordX.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(coordX, Priority.ALWAYS);

        coordY.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(coordY, Priority.ALWAYS);
        HBox yRow = new HBox(8, makeStar(), coordY);
        yRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(yRow, Priority.ALWAYS);

        xyBox.getChildren().addAll(coordX, yRow);

        VBox coordVBox = new VBox(2, coordLabel, xyBox);
        coordVBox.setAlignment(Pos.CENTER_LEFT);

        HBox coordContainer = new HBox(8, makeEmptyStar(), coordVBox);
        coordContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(coordVBox, Priority.ALWAYS);

        // 6. Жанр
        VBox genreVBox = new VBox(2, genreLabel, genreBox);
        genreVBox.setAlignment(Pos.CENTER_LEFT);
        genreBox.setMaxWidth(Double.MAX_VALUE);
        HBox genreContainer = new HBox(8, starGenre, genreVBox);
        genreContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(genreVBox, Priority.ALWAYS);

        // 7. Дата основания
        VBox dateVBox = new VBox(2, dateLabel, datePicker);
        dateVBox.setAlignment(Pos.CENTER_LEFT);
        datePicker.setMaxWidth(Double.MAX_VALUE);
        HBox dateContainer = new HBox(8, starDate, dateVBox);
        dateContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(dateVBox, Priority.ALWAYS);

        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(cancelButton, saveButton);

        fieldsBox.getChildren().addAll(
                nameContainer,
                participantsContainer,
                albumsContainer,
                salesContainer,
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
        datePicker.setEditable(false);

        Font labelFont = Font.font("Verdana", 12);
        String labelStyle = "-fx-text-fill: white;";

        nameLabel.setFont(labelFont); nameLabel.setStyle(labelStyle);
        participantsLabel.setFont(labelFont); participantsLabel.setStyle(labelStyle);
        albumsLabel.setFont(labelFont); albumsLabel.setStyle(labelStyle);
        salesLabel.setFont(labelFont); salesLabel.setStyle(labelStyle);
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
        star.setMinWidth(12);
        star.setAlignment(Pos.CENTER_LEFT);
        return star;
    }

    private static Label makeEmptyStar() {
        Label star = new Label("*");
        star.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        star.setStyle("-fx-text-fill: transparent;");
        star.setMinWidth(12);
        star.setAlignment(Pos.CENTER_LEFT);
        return star;
    }
}