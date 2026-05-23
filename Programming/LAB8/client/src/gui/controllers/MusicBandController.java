package gui.controllers;

import commands.CommandClient;
import commands.collection.Add;
import commands.collection.AddIfMin;
import commands.collection.RemoveGreater;
import commands.collection.Update;
import common.models.Coordinates;
import common.models.MusicBand;
import gui.views.MusicBandView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main_classes.WindowManager;
import net.Client;

import java.util.Locale;
import java.util.ResourceBundle;

public class MusicBandController {
    final MusicBandView view;
    final Client client;
    final WindowManager windowManager;
    private ResourceBundle bundle;
    private final Stage currentStage;

    private final int targetId;
    private final Mode mode;

    private static final String DEFAULT_FIELD_STYLE = "-fx-background-color: #2A363F; -fx-text-fill: white; -fx-background-radius: 4; -fx-border-color: #808080; -fx-border-radius: 4;";
    private static final String ERROR_FIELD_STYLE = "-fx-background-color: #2A363F; -fx-text-fill: white; -fx-background-radius: 4; -fx-border-color: #ff6b6b; -fx-border-width: 2; -fx-border-radius: 4;";

    // Для отображения окна с данными MusicBand разных команд
    public enum Mode {
        ADD,
        ADD_IF_MIN,
        REMOVE_GREATER,
        UPDATE
    }

    public MusicBandController(MusicBandView view, Client client, WindowManager windowManager, Stage currentStage, Mode mode, int targetId) {
        this.view = view;
        this.client = client;
        this.windowManager = windowManager;
        this.currentStage = currentStage;
        this.mode = mode;
        this.targetId = targetId;

        initialize();
    }

    public MusicBandController(MusicBandView view, Client client, WindowManager windowManager, Stage currentStage, Mode mode) {
        this(view, client, windowManager, currentStage, mode, -1);
    }

    public MusicBandController(MusicBandView view, Client client, WindowManager windowManager, Stage currentStage) {
        this(view, client, windowManager, currentStage, Mode.ADD, -1);
    }

    private void initialize() {
        setupI18n();
        setupEvents();
    }

    private void setupEvents() {
        view.languageBox.setOnAction(e -> changeLanguage(view.languageBox.getValue()));
        view.cancelButton.setOnAction(e -> currentStage.close());
        view.saveButton.setOnAction(e -> handleSaveAction());
    }

    private void handleSaveAction() {
        resetStyles();
        if (!validateInput()) return;

        try {
            MusicBand band = buildMusicBandFromFields();
            executeCommand(band);
        } catch (Exception ex) {
            ex.printStackTrace();
            showRawError("Внутренняя ошибка сборки объекта");
        }
    }

    private MusicBand buildMusicBandFromFields() {
        String albumsText = view.albumsField.getText().trim();
        Long albums = albumsText.isEmpty() ? null : Long.parseLong(albumsText);

        return new MusicBand.Builder()
                .name(view.nameField.getText().trim())
                .numberOfParticipants(Integer.parseInt(view.participantsField.getText().trim()))
                .albumsCount(albums)
                .label(new common.models.Label(Double.parseDouble(view.salesField.getText().trim())))
                .coordinates(new Coordinates(
                        Integer.parseInt(view.coordX.getText().trim()),
                        Long.parseLong(view.coordY.getText().trim())
                ))
                .genre(common.models.MusicGenre.valueOf(view.genreBox.getValue()))
                .establishmentDate(view.datePicker.getValue())
                .build();
    }

    private void executeCommand(MusicBand band) {
        CommandClient cmd = switch (mode) {
            case ADD -> new Add(band);
            case ADD_IF_MIN -> new AddIfMin(band);
            case REMOVE_GREATER -> new RemoveGreater(band);
            case UPDATE -> new Update(band, String.valueOf(targetId));
        };

        client.sendCommandAsync(
                cmd,
                response -> currentStage.close(),
                errorMessage -> {
                    if ("NO_CONNECTION".equals(errorMessage)) {
                        showError("login.error.connect_to_server");
                    } else {
                        showRawError(errorMessage);
                    }
                }
        );
    }

    private boolean validateInput() {
        if (view.nameField.getText().trim().isEmpty()) {
            showFieldError(view.nameField, "band.error.empty_name");
            return false;
        }

        try {
            int participants = Integer.parseInt(view.participantsField.getText().trim());
            if (participants <= 0) {
                showFieldError(view.participantsField, "band.error.participants_zero");
                return false;
            }
        } catch (NumberFormatException e) {
            showFieldError(view.participantsField, "band.error.invalid_number");
            return false;
        }

        String albumsText = view.albumsField.getText().trim();
        if (!albumsText.isEmpty()) {
            try {
                long albums = Long.parseLong(albumsText);
                if (albums <= 0) {
                    showFieldError(view.albumsField, "band.error.albums_zero");
                    return false;
                }
            } catch (NumberFormatException e) {
                showFieldError(view.albumsField, "band.error.invalid_number");
                return false;
            }
        }

        try {
            double sales = Double.parseDouble(view.salesField.getText().trim());
            if (sales <= 0) {
                showFieldError(view.salesField, "band.error.sales_zero");
                return false;
            }
        } catch (NumberFormatException e) {
            showFieldError(view.salesField, "band.error.invalid_number");
            return false;
        }

        String xText = view.coordX.getText().trim();
        if (!xText.isEmpty()) {
            try {
                Integer.parseInt(xText);
            } catch (NumberFormatException e) {
                showFieldError(view.coordX, "band.error.invalid_coord");
                return false;
            }
        }

        try {
            Long.parseLong(view.coordY.getText().trim());
        } catch (NumberFormatException e) {
            showFieldError(view.coordY, "band.error.invalid_coord");
            return false;
        }

        if (view.datePicker.getValue() == null) {
            view.datePicker.setStyle(ERROR_FIELD_STYLE);
            showError("band.error.empty_date");
            return false;
        }

        return true;
    }

    private void showFieldError(TextField field, String errorKey) {
        field.setStyle(ERROR_FIELD_STYLE);
        showError(errorKey);
    }

    private void showError(String errorKey) {
        view.errorLabel.setText(bundle.getString(errorKey));
        view.errorLabel.setVisible(true);
        view.errorLabel.setManaged(true);
    }

    private void resetStyles() {
        view.errorLabel.setVisible(false);
        view.errorLabel.setManaged(false);

        view.nameField.setStyle(DEFAULT_FIELD_STYLE);
        view.participantsField.setStyle(DEFAULT_FIELD_STYLE);
        view.albumsField.setStyle(DEFAULT_FIELD_STYLE);
        view.salesField.setStyle(DEFAULT_FIELD_STYLE);
        view.coordX.setStyle(DEFAULT_FIELD_STYLE);
        view.coordY.setStyle(DEFAULT_FIELD_STYLE);
        view.datePicker.setStyle(DEFAULT_FIELD_STYLE);
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
        currentStage.setTitle(bundle.getString("band.title"));

        view.nameField.setPromptText(bundle.getString("band.field.name"));
        view.participantsField.setPromptText(bundle.getString("band.field.participants"));
        view.albumsField.setPromptText(bundle.getString("band.field.albums"));
        view.salesField.setPromptText(bundle.getString("band.field.sales"));
        view.coordX.setPromptText("X");
        view.coordY.setPromptText("Y");

        view.coordLabel.setText(bundle.getString("band.label.coord"));
        view.genreLabel.setText(bundle.getString("band.label.genre"));
        view.dateLabel.setText(bundle.getString("band.label.date"));

        view.saveButton.setText(bundle.getString("band.button.save"));
        view.cancelButton.setText(bundle.getString("band.button.cancel"));
    }

    private void showRawError(String msg) {
        view.errorLabel.setText(msg);
        view.errorLabel.setVisible(true);
        view.errorLabel.setManaged(true);
    }
}