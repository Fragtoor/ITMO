package gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ScriptView extends VBox {

    public final Label titleLabel = new Label();

    public final Label fileLabel    = new Label();
    public final TextField fileField = new TextField();

    public final Button cancelButton  = new Button();
    public final Button executeButton = new Button();
    public final Button browseButton = new Button();

    private static final String DARK         = "#1e262c";
    private static final String BORDER_COLOR = "#3a4a58";

    public ScriptView() {
        this.setStyle("-fx-background-color: #2A363F;");
        this.setSpacing(14);
        this.setPadding(new Insets(0, 0, 16, 0));

        this.getChildren().addAll(
                buildHeader(),
                buildContent(),
                buildButtons()
        );
    }

    private HBox buildHeader() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(10, 12, 10, 12));
        bar.setStyle(
                "-fx-background-color: " + DARK + ";" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-width: 0 0 1 0;"
        );

        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        titleLabel.setStyle("-fx-text-fill: white;");

        bar.getChildren().add(titleLabel);
        return bar;
    }

    private VBox buildContent() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(0, 16, 0, 16));

        fileLabel.setStyle(
                "-fx-text-fill: #aaaaaa;" +
                        "-fx-font-family: 'Verdana'; -fx-font-size: 12px;"
        );

        fileField.setEditable(false);

        fileField.setMaxWidth(Double.MAX_VALUE);
        fileField.setStyle(
                "-fx-background-color: " + DARK + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #555;" +
                        "-fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 4; -fx-border-width: 1;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-family: 'Verdana'; -fx-font-size: 13px;" +
                        "-fx-padding: 7 10 7 10;"
        );

        browseButton.setText("...");
        browseButton.setStyle(
                "-fx-background-color: #808080;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Verdana'; -fx-font-size: 13px;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 7 12 7 12;" +
                        "-fx-cursor: hand;"
        );

        HBox fieldRow = new HBox(8, fileField, browseButton);
        HBox.setHgrow(fileField, Priority.ALWAYS);
        fieldRow.setAlignment(Pos.CENTER_LEFT);

        box.getChildren().addAll(fileLabel, fieldRow);
        return box;
    }

    private HBox buildButtons() {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_RIGHT);
        row.setPadding(new Insets(0, 16, 0, 16));

        styleButton(cancelButton,  "#c0392b");
        styleButton(executeButton, "#27ae60");

        row.getChildren().addAll(cancelButton, executeButton);
        return row;
    }

    private void styleButton(Button btn, String color) {
        btn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btn, Priority.ALWAYS);
        btn.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Verdana'; -fx-font-size: 13px;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 7 0 7 0;" +
                        "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
    }
}