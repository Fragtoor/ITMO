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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main_classes.WindowManager;
import net.Client;
import tools.ScriptExecutor;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController {
    final MainView view;
    final Client client;
    final WindowManager windowManager;
    private ResourceBundle bundle;
    private final User user;
    private final Stage currentStage;
    private ObservableList<MusicBand> allBands = FXCollections.observableArrayList();
    private final ScriptExecutor scriptExecutor;

    public MainController(MainView view, Client client, WindowManager windowManager, User user, Stage stage) {
        this.view = view;
        this.client = client;
        this.windowManager = windowManager;
        this.user = user;
        this.currentStage = stage;
        scriptExecutor = new ScriptExecutor(client);
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

        // Первичная загрузка данных с учетом текущего состояния фильтров
        handleSearch(view.searchField.getText());
        startPeriodicUpdate();
    }

    private void startPeriodicUpdate() {
        // Фоновое обновление, которое сохраняет все введенные фильтры
        Timeline updater = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            handleSearch(view.searchField.getText());
        }));
        updater.setCycleCount(Animation.INDEFINITE);
        updater.play();
    }

    private void setupGeneralButtons() {
        view.languageBox.setOnAction(e -> changeLanguage(view.languageBox.getValue()));
        view.logoutButton.setOnAction(e -> windowManager.showLoginWindow());
        view.addButton.setOnAction(e -> {
            windowManager.showMusicBandWindow();
            handleSearch(view.searchField.getText());
        });
        view.clearButton.setOnAction(e -> executeActionCommand(new Clear()));
        view.scriptButton.setOnAction(e -> openScriptDialog());
    }

    private void setupConsoleCommands() {
        view.helpButton.setOnAction(e -> executeTextCommand(new Help(), "[ПОМОЩЬ]: "));
        view.infoButton.setOnAction(e -> executeTextCommand(new Info(), "[ИНФО]: "));
        view.historyButton.setOnAction(e -> executeListCommand(new History(), "[ИСТОРИЯ]: "));
    }

    private void setupMenus() {
        MenuItem removeByIdItem = new MenuItem("remove__by__id");
        removeByIdItem.setOnAction(e -> requestAndExecuteRemoveById());

        MenuItem addIfMinItem = new MenuItem("add__if__min");
        addIfMinItem.setOnAction(e -> windowManager.showAddIfMinWindow());

        MenuItem removeGreaterItem = new MenuItem("remove__greater");
        removeGreaterItem.setOnAction(e -> windowManager.showRemoveGreaterWindow());

        MenuItem updateItem = new MenuItem("update");
        updateItem.setOnAction(e -> windowManager.showUpdateWindow());

        view.manageMenu.getItems().addAll(updateItem, removeByIdItem, addIfMinItem, removeGreaterItem);

        MenuItem sumItem = new MenuItem("sum__of__number__of__participants");
        sumItem.setOnAction(e -> executeTextCommand(new SumOfNumberOfParticipants(), "[ВЫЧИСЛЕНИЯ]: "));

        MenuItem avgItem = new MenuItem("average__of__number__of__participants");
        avgItem.setOnAction(e -> executeTextCommand(new AverageOfNumberOfParticipants(), "[ВЫЧИСЛЕНИЯ]: "));

        view.calcMenu.getItems().addAll(sumItem, avgItem);
    }

    private void setupFilters() {
        view.searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch(newValue));
        view.filterApply.setOnAction(e -> applyFilter());
        view.filterClear.setOnAction(e -> clearFilter());
    }

    private void setupTableAndVisualization() {
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
            printToConsole("[ОШИБКА]: Вы можете редактировать только свои элементы");
            return;
        }
        windowManager.showUpdateWindowWithBand(selected);
        handleSearch(view.searchField.getText());
    }

    private void executeActionCommand(CommandClient command) {
        client.sendCommandAsync(command, response -> handleSearch(view.searchField.getText()), this::showError);
    }

    private void executeTextCommand(CommandClient command, String prefix) {
        client.sendCommandAsync(command,
                response -> printToConsole(prefix + "\n" + response.getMessage()),
                this::showError
        );
    }

    @SuppressWarnings("unchecked")
    private void executeListCommand(CommandClient command, String prefix) {
        client.sendCommandAsync(command,
                response -> {
                    List<String> res = (List<String>) response.getObj();
                    StringBuilder sb = new StringBuilder(prefix).append("\n");
                    int i = 1;
                    for (String line : res) {
                        if (!line.isBlank()) {
                            sb.append(i++).append(") ").append(line).append("\n");
                        }
                    }
                    printToConsole(sb.toString());
                },
                this::showError
        );
    }

    private void printToConsole(String message) {
        for (String line : message.split("\n")) {
            if (!line.isBlank()) {
                view.consoleLines.add(line);
            }
        }
        view.consoleLines.add("");
        view.consoleView.scrollTo(view.consoleLines.size());
    }

    private void requestAndExecuteRemoveById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("remove_by_id");
        dialog.setHeaderText(null);
        dialog.setContentText("Введите id:");
        dialog.showAndWait().ifPresent(idStr -> executeActionCommand(new RemoveById(idStr)));
    }

    private void openScriptDialog() {
        ScriptView scriptView = new ScriptView();
        scriptView.browseButton.setOnAction(ev -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(bundle.getString("script.title"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
            File file = fileChooser.showOpenDialog(currentStage);
            if (file != null) {
                scriptView.fileField.setText(file.getAbsolutePath());
            }
        });

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
                scriptExecutor.run(path, this::printToConsole);
                handleSearch(view.searchField.getText());
            }
        });
    }

    // Единственный метод для получения данных, который учитывает и строку поиска, и фильтры
    private void handleSearch(String query) {
        String trimmedQuery = query == null ? "" : query.trim();

        // Если поиск пустой - запрашиваем всё (Show), иначе ищем по имени
        CommandClient cmd = trimmedQuery.isBlank() ? new Show() : new FilterContainsName(trimmedQuery);

        client.sendCommandAsync(cmd,
                response -> {
                    @SuppressWarnings("unchecked")
                    ArrayList<MusicBand> bands = (ArrayList<MusicBand>) response.getObj();
                    allBands = FXCollections.observableArrayList(bands);
                    view.visualizationView.setBands(bands);

                    // После применения серверного поиска (allBands), применяем локальный фильтр по колонкам, если он задан
                    if (view.filterCol.getValue() != null && !view.filterValue.getText().isBlank()) {
                        applyFilter();
                    } else {
                        view.table.setItems(allBands);
                    }
                },
                this::showError
        );
    }

    private void clearFilter() {
        view.filterCol.setValue(null);
        view.filterCond.setValue(null);
        view.filterValue.setText("");
        view.table.setItems(allBands);
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
        printToConsole("[ОШИБКА]: " + msg);
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

    private boolean compareNumbers(Number fieldVal, String cond, String val) {
        double field = fieldVal.doubleValue();
        double target = Double.parseDouble(val);
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
            if (dateVal instanceof LocalDate ld) {
                LocalDate target = LocalDate.parse(val);
                return evaluateDateCondition(ld, target, cond, val);
            } else if (dateVal instanceof LocalDateTime ldt) {
                LocalDate target = LocalDate.parse(val);
                LocalDate ldtDate = ldt.toLocalDate();
                return evaluateDateCondition(ldtDate, target, cond, val);
            }
        } catch (DateTimeParseException e) {
            return false;
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
}