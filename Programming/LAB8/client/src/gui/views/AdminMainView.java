package gui.views;

import common.net.User;
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

public class AdminMainView extends VBox {
    public final Label usernameLabel = new Label();
    public final Label loginValueLabel = new Label();
    public final ComboBox<String> languageBox = new ComboBox<>();
    public final Button logoutButton = new Button("Выход");

    public final Button blockButton = new Button("Заблокировать");
    public final Button unblockButton = new Button("Разблокировать");
    public final Button updateRoleButton = new Button("Изменить роль");
    public final TextField searchField = new TextField();

    public final TabPane tabPane = new TabPane();
    public final Tab tableTab = new Tab("Таблица пользователей");
    public final Tab visualizationTab = new Tab("Визуализация");

    public final TableView<User> table = new TableView<>();
    public final AdminVisualizationView visualizationView = new AdminVisualizationView();

    public final TableColumn<User, Integer> idCol = new TableColumn<>("ID");
    public final TableColumn<User, String> nameCol = new TableColumn<>("Имя");
    public final TableColumn<User, String> loginCol = new TableColumn<>("Логин");
    public final TableColumn<User, String> roleCol = new TableColumn<>("Роль");
    public final TableColumn<User, String> statusCol = new TableColumn<>("Статус"); // Новая колонка

    public final ListView<String> rolesList = new ListView<>();
    public final Label rolesTitle = new Label("Все роли");
    public final Button createPermissionButton = new Button("Добавить функциональность");
    public final Button createRoleButton = new Button("Создать новую роль");
    public final Button deleteRoleButton = new Button("Удалить роль");
    public final Button deletePermissionButton = new Button("Удалить функциональность");

    public final Label consoleLabel = new Label("Консоль администратора");
    public final ObservableList<String> consoleLines = FXCollections.observableArrayList();
    public final ListView<String> consoleView = new ListView<>(consoleLines);

    private static final String BORDER_COLOR = "#3a4a58";
    private static final String GRAY = "#2A363F";

    public AdminMainView() {
        this.setStyle("-fx-background-color: " + GRAY + ";");
        SplitPane splitPane = new SplitPane(buildMainLayout(), buildConsole());
        splitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        splitPane.setDividerPositions(0.75);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        setupCellFactories();
        this.getChildren().addAll(buildTopBar(), buildToolBar(), splitPane);
    }

    private HBox buildTopBar() {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(8, 16, 8, 16));
        bar.setStyle("-fx-background-color: " + GRAY + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 0 1 0;");
        usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        usernameLabel.setStyle("-fx-text-fill: #808080;");
        loginValueLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        languageBox.setPrefWidth(150);
        languageBox.setStyle("-fx-background-color: #808080; -fx-text-fill: white; -fx-background-radius: 5;");
        logoutButton.setStyle("-fx-background-color: #808080; -fx-text-fill: " + GRAY + "; -fx-border-radius: 4; -fx-cursor: hand;");
        bar.getChildren().addAll(usernameLabel, loginValueLabel, spacer, languageBox, logoutButton);
        return bar;
    }

    private HBox buildToolBar() {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(8, 16, 8, 16));
        bar.setStyle("-fx-background-color: " + GRAY + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 0 1 0;");

        styleButton(blockButton, "#c0392b");
        styleButton(unblockButton, "#27ae60");
        styleButton(updateRoleButton, "#2980b9");

        searchField.setPrefWidth(250);
        searchField.setPromptText("🔍 Поиск по имени...");
        searchField.setStyle("-fx-background-color: #808080; -fx-text-fill: white; -fx-prompt-text-fill: #ddd; -fx-background-radius: 4;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        bar.getChildren().addAll(blockButton, unblockButton, updateRoleButton, spacer, searchField);
        return bar;
    }

    private TabPane buildMainLayout() {
        table.getColumns().addAll(idCol, nameCol, loginCol, roleCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-base: " + GRAY + "; -fx-control-inner-background: " + GRAY + "; -fx-text-fill: white;");
        HBox.setHgrow(table, Priority.ALWAYS);

        VBox rolesPanel = new VBox(10);
        rolesPanel.setPadding(new Insets(10));
        rolesPanel.setStyle("-fx-background-color: " + GRAY + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 0 0 1px;");
        rolesPanel.setPrefWidth(280);
        rolesPanel.setMinWidth(280);

        rolesTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        rolesTitle.setStyle("-fx-text-fill: white;");

        rolesList.setStyle("-fx-control-inner-background: #1e262c; -fx-text-fill: white; -fx-font-family: 'Verdana'; -fx-font-size: 14px;");
        VBox.setVgrow(rolesList, Priority.ALWAYS);

        styleButton(createPermissionButton, "#27ae60");
        createPermissionButton.setMaxWidth(Double.MAX_VALUE);

        styleButton(createRoleButton, "#e67e22");
        createRoleButton.setMaxWidth(Double.MAX_VALUE);

        styleButton(deleteRoleButton, "#c0392b");
        deleteRoleButton.setMaxWidth(Double.MAX_VALUE);

        styleButton(deletePermissionButton, "#c0392b");
        deletePermissionButton.setMaxWidth(Double.MAX_VALUE);

        rolesPanel.getChildren().addAll(rolesTitle, rolesList, createRoleButton, deleteRoleButton, createPermissionButton, deletePermissionButton);

        HBox tableAndRoles = new HBox(table, rolesPanel);
        tableTab.setClosable(false);
        tableTab.setContent(tableAndRoles);

        visualizationTab.setClosable(false);
        visualizationTab.setContent(visualizationView);

        tabPane.getTabs().addAll(tableTab, visualizationTab);
        tabPane.setStyle("-fx-background-color: " + GRAY + ";");
        return tabPane;
    }

    private VBox buildConsole() {
        consoleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        consoleView.setPrefHeight(110);
        consoleView.setStyle("-fx-background-color: #0d1117; -fx-control-inner-background: #0d1117;");
        VBox.setVgrow(consoleView, Priority.ALWAYS);
        VBox box = new VBox(4, consoleLabel, consoleView);
        box.setStyle("-fx-background-color: #232e36;");
        return box;
    }

    private void styleButton(Button btn, String color) {
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 6 12 6 12;");
    }

    private void setupCellFactories() {
        idCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        nameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getUserName()));
        loginCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getLogin()));
        roleCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getRole()));
        statusCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().isBanned() ? "Заблокирован" : "Активен"));
    }
}