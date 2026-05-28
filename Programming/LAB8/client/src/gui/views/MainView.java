package gui.views;

import common.models.MusicBand;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainView extends VBox {
    // Верхняя панель
    public final Label usernameLabel = new Label();
    public final Label loginValueLabel = new Label();
    public final ComboBox<String> languageBox = new ComboBox<>();
    public final Button logoutButton = new Button();

    // Панель инструментов
    public final Button addButton = new Button();
    public final Button clearButton = new Button();
    public final Button scriptButton = new Button();

    public final MenuButton manageMenu = new MenuButton();
    public final MenuButton calcMenu = new MenuButton();

    public final Button helpButton = new Button("?");
    public final Button infoButton = new Button("ℹ");
    public final Button historyButton = new Button("◴");

    // Панель фильтра
    public final TextField searchField = new TextField();
    public Label filterLabel = new Label();
    public final ComboBox<String> filterCol = new ComboBox<>();
    public final ComboBox<String> filterCond = new ComboBox<>();
    public final TextField filterValue = new TextField();
    public final Button filterApply = new Button("✓");
    public final Button filterClear = new Button("✗");

    public final Tab visualizationTab = new Tab();
    public final Tab tableTab = new Tab();
    public final TabPane tabPane = new TabPane();

    public final TableView<MusicBand> table = new TableView<>();
    public final VisualizationView visualizationView = new VisualizationView();

    public final TableColumn<MusicBand, Integer> idCol = new TableColumn<>("ИД");
    public final TableColumn<MusicBand, String>  nameCol = new TableColumn<>("Название");
    public final TableColumn<MusicBand, Integer> coordXCol = new TableColumn<>("X");
    public final TableColumn<MusicBand, Long>    coordYCol = new TableColumn<>("Y");
    public final TableColumn<MusicBand, String>  creationDateCol = new TableColumn<>("Создано");
    public final TableColumn<MusicBand, String>  establishmentDateCol = new TableColumn<>("Создание объекта");
    public final TableColumn<MusicBand, Integer> participantsCol = new TableColumn<>("Участники");
    public final TableColumn<MusicBand, Long>    albumsCol = new TableColumn<>("Альбомы");
    public final TableColumn<MusicBand, String>  genreCol = new TableColumn<>("Жанр");
    public final TableColumn<MusicBand, Double>  labelCol = new TableColumn<>("Сборы");

    // Консоль
    public Label consoleLabel = new Label();
    public final ObservableList<String> consoleLines = FXCollections.observableArrayList();
    public final ListView<String> consoleView = new ListView<>(consoleLines);

    private static final String BORDER_COLOR = "#3a4a58";
    private static final String GRAY = "#2A363F";

    public MainView() {
        this.setStyle("-fx-background-color: " + GRAY + ";");

        SplitPane splitPane = new SplitPane(buildTabPane(), buildConsole());
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        splitPane.setDividerPositions(0.75);
        splitPane.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        setupCellFactories();

        this.getChildren().addAll(
                buildTopBar(),
                buildToolBar(),
                buildFilterBar(),
                splitPane
        );
    }

    // Верхняя панель
    private HBox buildTopBar() {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(8, 16, 8, 16));
        bar.setStyle(
                "-fx-background-color: " + GRAY + ";" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-width: 0 0 1 0;"
        );

        usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        usernameLabel.setStyle("-fx-text-fill: #808080; -fx-font-family: 'Verdana'; -fx-font-size: 13px;");
        loginValueLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Verdana'; -fx-font-size: 13px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        languageBox.setPrefWidth(150);
        languageBox.setStyle("-fx-background-color: #808080; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 5;" +
                "-fx-font-family: 'Verdana';"
        );

        logoutButton.setStyle(
                "-fx-background-color: #808080;" +
                        "-fx-text-fill: " + GRAY + ";" +
                        "-fx-border-radius: 4; -fx-border-width: 1;" +
                        "-fx-font-family: 'Verdana'; -fx-font-size: 13px;" +
                        "-fx-padding: 4 14 4 14;" +
                        "-fx-cursor: hand;"
        );

        bar.getChildren().addAll(usernameLabel, loginValueLabel, spacer, languageBox, logoutButton);
        return bar;
    }

    // Панель управления
    private HBox buildToolBar() {
        HBox bar = new HBox(8);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(8, 16, 8, 16));
        bar.setStyle(
                "-fx-background-color: " + GRAY + ";" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-width: 0 0 1 0;"
        );

        styleColorButton(addButton, "#27ae60");
        styleColorButton(clearButton, "#c0392b");
        styleColorButton(scriptButton, "#808080");

        styleMenuButton(manageMenu);
        styleMenuButton(calcMenu);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        styleIconButton(helpButton);
        styleIconButton(infoButton);
        styleIconButton(historyButton);

        bar.getChildren().addAll(
                addButton, clearButton, scriptButton,
                manageMenu, calcMenu,
                spacer,
                helpButton, infoButton, historyButton
        );
        return bar;
    }

    private HBox buildFilterBar() {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(8, 16, 8, 16));
        bar.setStyle(
                "-fx-background-color: " + GRAY + ";" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-width: 0 0 1 0;"
        );

        searchField.setPrefWidth(200);
        styleTextField(searchField);

        filterLabel = new Label();
        filterLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Verdana'; -fx-font-size: 13px;");

        filterCol.getItems().addAll("id", "name", "genre", "numberOfParticipants",
                "albumsCount", "X", "Y", "creationDate", "establishmentDate", "label");
        filterCol.setPrefWidth(130);
        styleComboBox(filterCol);

        filterCond.getItems().addAll("=", "≠", ">", "<", "≥", "≤", "содержит");
        filterCond.setPrefWidth(120);
        styleComboBox(filterCond);

        filterValue.setPrefWidth(140);
        styleTextField(filterValue);

        filterApply.setStyle(
                "-fx-background-color: #27ae60;" +
                        "-fx-text-fill: white; -fx-font-size: 14px;" +
                        "-fx-background-radius: 5; -fx-cursor: hand;" +
                        "-fx-pref-width: 34; -fx-pref-height: 30;"
        );
        filterClear.setStyle(
                "-fx-background-color: #c0392b;" +
                        "-fx-text-fill: white; -fx-font-size: 14px;" +
                        "-fx-background-radius: 5; -fx-cursor: hand;" +
                        "-fx-pref-width: 34; -fx-pref-height: 30;"
        );

        bar.getChildren().addAll(
                searchField, filterLabel,
                filterCol, filterCond, filterValue,
                filterApply, filterClear
        );
        return bar;
    }

    // Таблица
    private TabPane buildTabPane() {
        table.getColumns().addAll(
                idCol, nameCol, coordXCol, coordYCol,
                creationDateCol, establishmentDateCol,
                participantsCol, albumsCol, genreCol, labelCol
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle(
                "-fx-base: " + GRAY + ";" +
                        "-fx-control-inner-background: " + GRAY + ";" +
                        "-fx-table-cell-border-color: " + BORDER_COLOR + ";" +
                        "-fx-table-header-border-color: " + BORDER_COLOR + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: " + GRAY + ";"
        );
        VBox.setVgrow(table, Priority.ALWAYS);

        // Вкладка "Таблица данных"
        tableTab.setClosable(false);
        tableTab.setContent(table);

        // Вкладка "Визуализация"
        visualizationTab.setClosable(false);
        visualizationTab.setContent(visualizationView);

        tabPane.getTabs().addAll(visualizationTab, tableTab);
        tabPane.getSelectionModel().select(tableTab);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        tabPane.setStyle(
                "-fx-tab-min-width: 120px;" +
                        "-fx-background-color: " + GRAY + ";"
        );

        return tabPane;
    }

    // Консоль вывода
    private VBox buildConsole() {
        consoleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-family: 'Verdana'; -fx-font-size: 12px; -fx-font-weight: bold;"
        );

        consoleView.setMaxHeight(Double.MAX_VALUE);

        consoleView.setPrefHeight(110);
        consoleView.setFixedCellSize(20);
        consoleView.setStyle(
                "-fx-background-color: #0d1117;" +
                        "-fx-control-inner-background: #0d1117;" +
                        "-fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1;" +
                        "-fx-background-radius: 4;" +
                        "-fx-scroll-bar-policy: always;"
        );

        VBox.setVgrow(consoleView, Priority.ALWAYS);
        HBox.setHgrow(consoleView, Priority.ALWAYS);
        consoleView.setMaxWidth(Double.MAX_VALUE);
        VBox box = new VBox(4, consoleLabel, consoleView);
        box.setFillWidth(true);
        box.setMaxWidth(Double.MAX_VALUE);

        box.setStyle("-fx-background-color: #232e36;");
        return box;
    }

    private void styleColorButton(Button btn, String color) {
        btn.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Verdana'; -fx-font-size: 13px;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 5 14 5 14;" +
                        "-fx-pref-height: 30;" +
                        "-fx-cursor: hand;"
        );
    }

    private void styleMenuButton(MenuButton btn) {
        btn.setStyle(
                "-fx-background-color: #808080;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Verdana'; -fx-font-size: 13px;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 5 12 5 12;" +
                        "-fx-cursor: hand;"
        );
    }

    private void styleIconButton(Button btn) {
        btn.setStyle(
                "-fx-background-color: #808080;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-background-radius: 5;" +
                        "-fx-pref-width: 34; -fx-pref-height: 30;" +
                        "-fx-cursor: hand;"
        );
    }

    private void styleTextField(TextField tf) {
        tf.setStyle(
                "-fx-background-color: #808080;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #666;" +
                        "-fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 4; -fx-border-width: 1;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-family: 'Verdana'; -fx-font-size: 12px;" +
                        "-fx-padding: 4 8 4 8;"
        );
    }

    private void styleComboBox(ComboBox<?> cb) {
        cb.setStyle(
                "-fx-background-color: #808080;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-family: 'Verdana'; -fx-font-size: 12px;"
        );
    }

    private void setupCellFactories() {
        // Отображение MusicBand в таблице

        idCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        nameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
        participantsCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getNumberOfParticipants()));
        albumsCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getAlbumsCount()));

        creationDateCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getCreationDate() != null ? data.getValue().getCreationDate().toString() : ""
        ));
        establishmentDateCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getEstablishmentDate() != null ? data.getValue().getEstablishmentDate().toString() : ""
        ));

        genreCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getGenre() != null ? data.getValue().getGenre().toString() : ""
        ));

        coordXCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(
                data.getValue().getCoordinates() != null ? data.getValue().getCoordinates().getX() : null
        ));
        coordYCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(
                data.getValue().getCoordinates() != null ? data.getValue().getCoordinates().getY() : null
        ));

        labelCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(
                data.getValue().getLabel() != null ? data.getValue().getLabel().getSales() : null
        ));
    }
}
