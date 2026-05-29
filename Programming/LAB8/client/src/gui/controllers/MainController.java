package gui.controllers;

import commands.CommandClient;
import commands.collection.*;
import commands.other.Help;
import common.models.MusicBand;
import common.net.User;
import gui.views.MainView;
import gui.views.ScriptView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main_classes.WindowManager;
import net.Client;
import tools.ScriptExecutor;

import java.io.File;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController {
    private final MainView view;
    private final Client client;
    private final WindowManager windowManager;
    private final User user;
    private final Stage currentStage;
    private final ScriptExecutor scriptExecutor;

    private Timeline updater;

    private ResourceBundle bundle;
    private Locale currentLocale = new Locale("ru", "RU"); // Текущая локаль
    private ObservableList<MusicBand> allBands = FXCollections.observableArrayList();
    private enum LogType {
        INFO("console.prefix.info"),
        ERROR("console.prefix.error"),
        SUCCESS("console.prefix.success");

        private final String key;

        LogType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    // Контейнер лога
    private static class LogRecord {
        final LogType type;
        final String headerKey;
        final List<String> lines;
        final String rawMessage;

        LogRecord(LogType type, String rawMessage) {
            this.type = type;
            this.rawMessage = rawMessage;
            this.headerKey = null;
            this.lines = null;
        }

        LogRecord(LogType type, String headerKey, List<String> lines) {
            this.type = type;
            this.headerKey = headerKey;
            this.lines = lines;
            this.rawMessage = null;
        }
    }

    private final List<LogRecord> consoleHistory = new ArrayList<>();

    public MainController(MainView view, Client client, WindowManager windowManager, User user, Stage stage) {
        this.view = view;
        this.client = client;
        this.windowManager = windowManager;
        this.user = user;
        this.currentStage = stage;
        this.scriptExecutor = new ScriptExecutor(client);
        initialize();
    }

    private void initialize() {
        setupI18n();
        updateTexts();
        setupGeneralButtons();
        setupConsoleCommands();
        setupMenus();
        setupFilters();
        setupTableAndVisualization();

        // Прокручиваем элемент к последнему элементу
        view.consoleLines.addListener((javafx.collections.ListChangeListener.Change<? extends String> c) -> {
            view.consoleView.scrollTo(view.consoleLines.size() - 1);
        });

        handleSearch(view.searchField.getText());
        startPeriodicUpdate();
    }

    private void startPeriodicUpdate() {
        updater = new Timeline(new KeyFrame(Duration.seconds(2), event -> handleSearch(view.searchField.getText())));
        updater.setCycleCount(Animation.INDEFINITE);
        updater.play();
    }

    private void setupGeneralButtons() {
        view.languageBox.setOnAction(e -> changeLanguage(view.languageBox.getValue()));
        view.logoutButton.setOnAction(e -> {
            if (updater != null) {
                updater.stop();
            }
            windowManager.showLoginWindow();
        });

        view.addButton.setOnAction(e -> {
            windowManager.showMusicBandWindow();
            handleSearch(view.searchField.getText());
        });
        view.clearButton.setOnAction(e -> handleDeleteSelected());
        view.scriptButton.setOnAction(e -> openScriptDialog());
    }


    private void handleDeleteSelected() {
        MusicBand selected = view.table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("client.error.no_selection");
            return;
        }
        if (!selected.isOwner()) {
            showError("client.error.not_your_element");
            return;
        }
        executeActionCommand(new RemoveById(String.valueOf(selected.getId())));
    }

    private void setupConsoleCommands() {
        view.helpButton.setOnAction(e -> executeTextCommand(new Help(), "console.header.help"));
        view.infoButton.setOnAction(e -> executeTextCommand(new Info(), "console.header.info"));
        view.historyButton.setOnAction(e -> executeListCommand(new History(), "console.header.history"));
    }

    private void setupMenus() {
        MenuItem removeByIdItem = new MenuItem("remove__by__id");
        removeByIdItem.setOnAction(e -> requestAndExecuteRemoveById());

        MenuItem addIfMinItem = new MenuItem("add__if__min");
        addIfMinItem.setOnAction(e -> {
            windowManager.showAddIfMinWindow();
            handleSearch(view.searchField.getText());
        });

        MenuItem removeGreaterItem = new MenuItem("remove__greater");
        removeGreaterItem.setOnAction(e -> {
            windowManager.showRemoveGreaterWindow();
            handleSearch(view.searchField.getText());
        });

        MenuItem updateItem = new MenuItem("update");
        updateItem.setOnAction(e -> handleUpdateMenuAction());

        MenuItem clearItem = new MenuItem("clear");
        clearItem.setOnAction(e -> executeActionCommand(new Clear()));

        MenuItem sumItem = new MenuItem("sum__of__number__of__participants");
        sumItem.setOnAction(e -> executeTextCommand(new SumOfNumberOfParticipants(), "console.header.sum"));

        MenuItem avgItem = new MenuItem("average__of__number__of__participants");
        avgItem.setOnAction(e -> executeTextCommand(new AverageOfNumberOfParticipants(), "console.header.avg"));

        view.manageMenu.getItems().addAll(updateItem, removeByIdItem, addIfMinItem, removeGreaterItem, clearItem);
        view.calcMenu.getItems().addAll(sumItem, avgItem);
    }

    private void handleUpdateMenuAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("main.dialog.update.title"));
        dialog.setHeaderText(null);
        dialog.setContentText(bundle.getString("main.dialog.update.header"));
        dialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr.trim());
                MusicBand targetBand = allBands.stream()
                        .filter(band -> band.getId() == id)
                        .findFirst()
                        .orElse(null);

                if (targetBand == null) {
                    showError("client.error.id_not_found::" + id);
                    return;
                }

                if (!targetBand.isOwner()) {
                    showError("client.error.not_your_element");
                    return;
                }

                windowManager.showUpdateWindowWithBand(targetBand);
                handleSearch(view.searchField.getText());
            } catch (NumberFormatException ex) {
                showError("client.error.id_must_be_integer");
            }
        });
    }

    private void setupFilters() {
        view.searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch(newValue));
        view.filterApply.setOnAction(e -> applyFilter());
        view.filterClear.setOnAction(e -> clearFilter());
    }

    private void setupTableAndVisualization() {
        view.table.setRowFactory(tv -> new javafx.scene.control.TableRow<>() {
            protected void updateItem(MusicBand item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.isOwner()) {
                    setStyle("-fx-control-inner-background: #2d4a3e;");
                } else {
                    setStyle("");
                }
            }
        });
        view.table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                handleBandSelection(view.table.getSelectionModel().getSelectedItem());
            }
        });
        view.visualizationView.setOnBandClick(this::handleBandSelection);
    }

    private void handleBandSelection(MusicBand selected) {
        if (selected == null) return;
        if (!selected.isOwner()) {
            logToConsole(LogType.ERROR, "client.error.not_your_element");
            return;
        }
        windowManager.showUpdateWindowWithBand(selected);
        handleSearch(view.searchField.getText());
    }

    private void executeActionCommand(CommandClient command) {
        client.sendCommandAsync(command, response -> {
            handleSearch(view.searchField.getText());
            if (response.getMessage() != null && !response.getMessage().isBlank()) {
                logToConsole(LogType.SUCCESS, response.getMessage());
            }
        }, this::showError);
    }

    private void executeTextCommand(CommandClient command, String defaultHeaderKey) {
        client.sendCommandAsync(command,
                response -> {
                    String msg = response.getMessage();
                    if (msg == null || msg.isBlank()) {
                        logToConsole(LogType.INFO, defaultHeaderKey);
                    } else {
                        logToConsole(LogType.INFO, msg);
                    }
                },
                this::showError
        );
    }

    private void executeListCommand(CommandClient command, String headerKey) {
        client.sendCommandAsync(command,
                response -> {
                    List<String> res = (List<String>) response.getObj();
                    logListToConsole(LogType.INFO, headerKey, res);
                },
                this::showError
        );
    }

    private void requestAndExecuteRemoveById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("main.dialog.remove.title"));
        dialog.setHeaderText(null);
        dialog.setContentText(bundle.getString("main.dialog.remove.header"));
        dialog.showAndWait().ifPresent(idStr -> executeActionCommand(new RemoveById(idStr)));
    }

    private void openScriptDialog() {
        ScriptView scriptView = new ScriptView();
        scriptView.browseButton.setOnAction(ev -> handleBrowseScriptFile(scriptView));

        scriptView.titleLabel.setText(bundle.getString("script.title"));
        scriptView.fileLabel.setText(bundle.getString("script.file_label"));
        scriptView.cancelButton.setText(bundle.getString("script.cancel"));
        scriptView.executeButton.setText(bundle.getString("script.execute"));

        Stage stage = new Stage();
        stage.setScene(new Scene(scriptView, 320, 160));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        scriptView.cancelButton.setOnAction(ev -> stage.close());
        scriptView.executeButton.setOnAction(ev -> {
            String path = scriptView.fileField.getText().trim();
            if (!path.isBlank()) {
                stage.close();
                scriptExecutor.run(path, (msg) -> logToConsole(LogType.INFO, msg), () -> handleSearch(view.searchField.getText()));
            }
        });
    }

    private void handleBrowseScriptFile(ScriptView scriptView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("script.title"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        File file = fileChooser.showOpenDialog(currentStage);
        if (file != null) {
            scriptView.fileField.setText(file.getAbsolutePath());
        }
    }

    private void handleSearch(String query) {
        String trimmedQuery = query == null ? "" : query.trim();
        CommandClient cmd = trimmedQuery.isBlank() ? new Show() : new FilterContainsName(trimmedQuery);
        client.sendCommandAsync(cmd,
                response -> {
                    ArrayList<MusicBand> bands = (ArrayList<MusicBand>) response.getObj();
                    allBands = FXCollections.observableArrayList(bands);
                    view.visualizationView.setBands(bands);

                    if (view.filterCol.getValue() != null && !view.filterValue.getText().isBlank()) {
                        applyFilter();
                    } else {
                        view.table.setItems(allBands);
                    }

                    view.table.refresh();
                },
                this::showError
        );
    }

    private void clearFilter() {
        view.filterCol.setValue(null);
        view.filterCond.setValue(null);
        view.filterValue.setText("");
        view.table.setItems(allBands);

        view.table.refresh();
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

        currentLocale = switch (langSelection) {
            case "NL / Nederlands" -> new Locale("nl", "NL");
            case "SV / Svenska"    -> new Locale("sv", "SE");
            case "EN / English"    -> new Locale("en", "AU");
            default                -> new Locale("ru", "RU");
        };
        bundle = ResourceBundle.getBundle("resources.properties.messages", currentLocale);
        updateTexts();
        updateTableFormatting();
        refreshConsole();
    }

    private void updateTableFormatting() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(currentLocale);
        NumberFormat numberFormat = NumberFormat.getInstance(currentLocale);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(currentLocale);

        view.creationDateCol.setCellValueFactory(data -> {
            if (data.getValue().getCreationDate() != null) {
                return new javafx.beans.property.ReadOnlyStringWrapper(data.getValue().getCreationDate().format(dateFormatter));
            }
            return new javafx.beans.property.ReadOnlyStringWrapper("");
        });

        view.establishmentDateCol.setCellValueFactory(data -> {
            if (data.getValue().getEstablishmentDate() != null) {
                return new javafx.beans.property.ReadOnlyStringWrapper(data.getValue().getEstablishmentDate().format(dateFormatter));
            }
            return new javafx.beans.property.ReadOnlyStringWrapper("");
        });

        view.labelCol.setCellFactory(column -> new TableCell<MusicBand, Double>() {
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });

        view.coordXCol.setCellFactory(column -> new TableCell<>() {
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(numberFormat.format(item));
            }
        });

        view.coordYCol.setCellFactory(column -> new TableCell<>() {
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(numberFormat.format(item));
            }
        });

        view.albumsCol.setCellFactory(column -> new TableCell<>() {
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(numberFormat.format(item));
            }
        });

        view.participantsCol.setCellFactory(column -> new TableCell<>() {
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(numberFormat.format(item));
            }
        });

        view.table.refresh();
    }

    private void updateTexts() {
        currentStage.setTitle(bundle.getString("main.title"));
        view.logoutButton.setText(bundle.getString("main.logout_button"));
        view.usernameLabel.setText(bundle.getString("main.button.user") + ": ");
        view.loginValueLabel.setText(user.getLogin());
        view.addButton.setText(bundle.getString("main.button.add"));
        view.clearButton.setText(bundle.getString("main.button.clear"));
        view.scriptButton.setText(bundle.getString("main.button.script"));
        view.manageMenu.setText(bundle.getString("main.button.manageMenu"));
        view.calcMenu.setText(bundle.getString("main.button.calcMenu"));
        view.searchField.setPromptText(bundle.getString("main.button.search"));
        view.filterLabel.setText(bundle.getString("main.button.filter_label"));
        view.filterCol.setPromptText(bundle.getString("main.button.filter_col"));
        view.filterCond.setPromptText(bundle.getString("main.button.filter_cond"));
        view.filterValue.setPromptText(bundle.getString("main.button.filter_value"));
        view.table.setPlaceholder(new Label(bundle.getString("main.table.not_content")));
        view.tableTab.setText(bundle.getString("main.table.name_table"));
        view.visualizationTab.setText(bundle.getString("main.table.name_visualization"));
        view.consoleLabel.setText(bundle.getString("main.console.title"));

        view.idCol.setText(bundle.getString("main.table.col.id"));
        view.nameCol.setText(bundle.getString("main.table.col.name"));
        view.coordXCol.setText(bundle.getString("main.table.col.x"));
        view.coordYCol.setText(bundle.getString("main.table.col.y"));
        view.creationDateCol.setText(bundle.getString("main.table.col.creation_date"));
        view.establishmentDateCol.setText(bundle.getString("main.table.col.establishment_date"));
        view.participantsCol.setText(bundle.getString("main.table.col.participants"));
        view.albumsCol.setText(bundle.getString("main.table.col.albums"));
        view.genreCol.setText(bundle.getString("main.table.col.genre"));
        view.labelCol.setText(bundle.getString("main.table.col.label"));
    }

    private void showError(String msg) {
        if ("server.auth.error.account_banned".equals(msg)) {
            if (updater != null) updater.stop();
            Platform.runLater(() -> {
                windowManager.showLoginWindow();

                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);

                alert.setTitle(bundle.getString("client.dialog.banned.title"));
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("server.auth.error.account_banned"));

                alert.showAndWait();
            });
            return;
        }

        logToConsole(LogType.ERROR, msg);
    }

    private boolean matchesFilter(MusicBand band, String col, String cond, String val) {
        try {
            return switch (col) {
                case "id" -> compareNumbers(band.getId(), cond, val);
                case "name" -> compareStrings(band.getName(), cond, val);
                case "genre" -> compareStrings(band.getGenre() != null ? band.getGenre().toString() : "", cond, val);
                case "numberOfParticipants" -> compareNumbers(band.getNumberOfParticipants(), cond, val);
                case "albumsCount" -> compareNumbers(band.getAlbumsCount() != null ? band.getAlbumsCount() : 0L, cond, val);
                case "X" -> compareNumbers(band.getCoordinates().getX(), cond, val);
                case "Y" -> compareNumbers(band.getCoordinates().getY(), cond, val);
                case "creationDate" -> compareDates(band.getCreationDate(), cond, val);
                case "establishmentDate" -> compareDates(band.getEstablishmentDate(), cond, val);
                case "label" -> compareNumbers(band.getLabel().getSales(), cond, val);
                default -> true;
            };
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void applyFilter() {
        String col = view.filterCol.getValue();
        String cond = view.filterCond.getValue();
        String val = view.filterValue.getText().trim();

        if (col == null || cond == null || val.isBlank()) {
            view.table.setItems(allBands);
            return;
        }

        ObservableList<MusicBand> filtered = allBands.stream()
                .filter(band -> matchesFilter(band, col, cond, val))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        view.table.setItems(filtered);

        view.table.refresh();
    }

    private boolean compareNumbers(Number fieldVal, String cond, String val) {
        double field = fieldVal.doubleValue();
        double target;

        try {
            target = NumberFormat.getInstance(currentLocale).parse(val).doubleValue();
        } catch (ParseException e) {
            try {
                target = Double.parseDouble(val);
            } catch (NumberFormatException ex) {
                return false;
            }
        }

        return switch (cond) {
            case "=" -> field == target;
            case "≠" -> field != target;
            case ">" -> field > target;
            case "<" -> field < target;
            case "≥" -> field >= target;
            case "≤" -> field <= target;
            case "содержит" -> String.valueOf((long) field).contains(val);
            default -> false;
        };
    }

    private boolean compareStrings(String fieldVal, String cond, String val) {
        if (fieldVal == null) return false;
        return switch (cond) {
            case "=" -> fieldVal.equalsIgnoreCase(val);
            case "≠" -> !fieldVal.equalsIgnoreCase(val);
            case "содержит" -> fieldVal.toLowerCase().contains(val.toLowerCase());
            case ">" -> fieldVal.compareToIgnoreCase(val) > 0;
            case "<" -> fieldVal.compareToIgnoreCase(val) < 0;
            case "≥" -> fieldVal.compareToIgnoreCase(val) >= 0;
            case "≤" -> fieldVal.compareToIgnoreCase(val) <= 0;
            default -> false;
        };
    }

    private boolean compareDates(Object dateVal, String cond, String val) {
        if (dateVal == null) return false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(currentLocale);
            LocalDate targetDate = LocalDate.parse(val, formatter);

            if (dateVal instanceof LocalDate ld) {
                return evaluateDateCondition(ld, targetDate, cond, val);
            } else if (dateVal instanceof LocalDateTime ldt) {
                return evaluateDateCondition(ldt.toLocalDate(), targetDate, cond, val);
            }
        } catch (DateTimeParseException e) {
            try {
                LocalDate targetDate = LocalDate.parse(val);
                if (dateVal instanceof LocalDate ld) {
                    return evaluateDateCondition(ld, targetDate, cond, val);
                } else if (dateVal instanceof LocalDateTime ldt) {
                    return evaluateDateCondition(ldt.toLocalDate(), targetDate, cond, val);
                }
            } catch (DateTimeParseException ex) {
                return false;
            }
        }
        return false;
    }

    private boolean evaluateDateCondition(LocalDate fieldDate, LocalDate targetDate, String cond, String val) {
        return switch (cond) {
            case "=" -> fieldDate.isEqual(targetDate);
            case "≠" -> !fieldDate.isEqual(targetDate);
            case ">" -> fieldDate.isAfter(targetDate);
            case "<" -> fieldDate.isBefore(targetDate);
            case "≥" -> !fieldDate.isBefore(targetDate);
            case "≤" -> !fieldDate.isAfter(targetDate);
            case "содержит" -> fieldDate.toString().contains(val);
            default -> false;
        };
    }

    private String translateServerMessage(String serverMsg) {
        if (serverMsg == null || serverMsg.isBlank()) return "";
        String[] parts = serverMsg.split("::");
        String key = parts[0];

        if (bundle.containsKey(key)) {
            if (parts.length > 1) {
                Object[] args = java.util.Arrays.copyOfRange(parts, 1, parts.length);
                return MessageFormat.format(bundle.getString(key), args);
            }
            return bundle.getString(key);
        }
        return serverMsg;
    }

    private void appendRecordToUi(LogRecord record) {
        String prefix = bundle.getString(record.type.getKey());
        if (record.rawMessage != null) {
            String translatedMessage = translateServerMessage(record.rawMessage);
            String[] lines = translatedMessage.split("\n");

            for (int i = 0; i < lines.length; i++) {
                if (i == 0) {
                    view.consoleLines.add(prefix + " " + lines[i]);
                } else {
                    view.consoleLines.add("    " + lines[i]);
                }
            }
        } else if (record.headerKey != null) {
            String header = bundle.containsKey(record.headerKey) ?
                    bundle.getString(record.headerKey) : record.headerKey;
            view.consoleLines.add(prefix + " " + header);

            if (record.lines != null && !record.lines.isEmpty()) {
                int i = 1;
                for (String line : record.lines) {
                    if (!line.isBlank()) {
                        view.consoleLines.add("    " + i++ + ") " + translateServerMessage(line));
                    }
                }
            }
        }
    }

    private void logToConsole(LogType type, String rawMessage) {
        LogRecord record = new LogRecord(type, rawMessage);
        consoleHistory.add(record);
        Platform.runLater(() -> appendRecordToUi(record));
    }

    private void logListToConsole(LogType type, String headerKey, List<String> lines) {
        LogRecord record = new LogRecord(type, headerKey, lines);
        consoleHistory.add(record);
        Platform.runLater(() -> appendRecordToUi(record));
    }

    private void refreshConsole() {
        Platform.runLater(() -> {
            view.consoleLines.clear();
            for (LogRecord record : consoleHistory) {
                appendRecordToUi(record);
            }
        });
    }
}