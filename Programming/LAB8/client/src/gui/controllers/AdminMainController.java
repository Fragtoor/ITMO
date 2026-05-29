package gui.controllers;

import commands.CommandClient;
import common.net.User;
import gui.views.AdminMainView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import main_classes.WindowManager;
import net.Client;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AdminMainController {
    private final AdminMainView view;
    private final Client client;
    private final WindowManager windowManager;
    private final User currentUser;
    private final Stage currentStage;

    private Timeline updater;
    private ObservableList<User> allUsers = FXCollections.observableArrayList();

    private ResourceBundle bundle;
    private Locale currentLocale = new Locale("ru", "RU");

    private Map<String, List<String>> rolesMap = new HashMap<>();

    private static class LogRecord {
        final String type;
        final String rawMessage;

        LogRecord(String type, String rawMessage) {
            this.type = type;
            this.rawMessage = rawMessage;
        }
    }

    private final List<LogRecord> consoleHistory = new ArrayList<>();

    public AdminMainController(AdminMainView view, Client client, WindowManager windowManager, User user, Stage stage) {
        this.view = view;
        this.client = client;
        this.windowManager = windowManager;
        this.currentUser = user;
        this.currentStage = stage;
        initialize();
    }

    private void initialize() {
        setupI18n();
        view.loginValueLabel.setText(currentUser.getLogin());

        view.logoutButton.setOnAction(e -> {
            if (updater != null) updater.stop();
            windowManager.showLoginWindow();
        });

        view.searchField.textProperty().addListener((obs, oldV, newV) -> filterUsers());

        view.blockButton.setOnAction(e -> handleSetUserStatus(true));
        view.unblockButton.setOnAction(e -> handleSetUserStatus(false));
        view.updateRoleButton.setOnAction(e -> handleUpdateRole());
        view.createRoleButton.setOnAction(e -> handleCreateRole());
        view.deleteRoleButton.setOnAction(e -> handleDeleteRole());
        view.createPermissionButton.setOnAction(e -> handleCreatePermission());
        view.deletePermissionButton.setOnAction(e -> handleDeletePermission());

        view.rolesList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String selectedRole = view.rolesList.getSelectionModel().getSelectedItem();
                if (selectedRole != null) showRoleDetailsDialog(selectedRole);
            }
        });

        view.rolesList.setCellFactory(lv -> new ListCell<String>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("ADMIN") || item.equals("USER") ||
                            item.equals("SUPERUSER") || item.equals("GUEST")) {
                        setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold; -fx-padding: 5px;");
                    } else {
                        setStyle("-fx-text-fill: white; -fx-font-weight: normal; -fx-padding: 5px;");
                    }
                }
            }
        });

        view.consoleLines.addListener((javafx.collections.ListChangeListener.Change<? extends String> c) -> {
            view.consoleView.scrollTo(view.consoleLines.size() - 1);
        });

        startPeriodicUpdate();
    }

    private void fetchUsersAndRoles() {
        client.sendCommandAsync(new commands.admin.ShowUsers(), response -> {
            List<User> users = (List<User>) response.getObj();
            if (users != null) {
                allUsers.setAll(users);
                filterUsers();
            }
        }, this::logError);

        client.sendCommandAsync(new commands.admin.GetRoles(), response -> {
            Map<String, List<String>> map = (Map<String, List<String>>) response.getObj();
            if (map != null) {
                rolesMap = map;
                Platform.runLater(() -> view.rolesList.getItems().setAll(rolesMap.keySet()));
            }
        }, this::logError);
    }

    private void startPeriodicUpdate() {
        fetchUsersAndRoles();
        updater = new Timeline(new KeyFrame(Duration.seconds(3), event -> fetchUsersAndRoles()));
        updater.setCycleCount(Animation.INDEFINITE);
        updater.play();
    }

    private void handleSetUserStatus(boolean block) {
        User selected = view.table.getSelectionModel().getSelectedItem();
        if (selected == null) { logError("admin.error.no_selection"); return; }
        executeActionCommand(new commands.admin.SetUserStatus(String.valueOf(selected.getId()), String.valueOf(block)));
    }

    private void handleUpdateRole() {
        User selected = view.table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            logError("admin.error.no_selection");
            return;
        }

        if (selected.getId() == currentUser.getId()) {
            logError("admin.error.cannot_change_own_role");
            return;
        }

        List<String> availableRoles = new ArrayList<>(rolesMap.keySet());
        String currentRole = selected.getRole();
        if (currentRole == null || !availableRoles.contains(currentRole)) {
            currentRole = availableRoles.get(0);
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(currentRole, availableRoles);
        dialog.setTitle(bundle.getString("admin.dialog.update_role.title"));
        dialog.setHeaderText(bundle.getString("admin.dialog.update_role.header") + " " + selected.getUserName());
        dialog.setContentText(bundle.getString("admin.dialog.update_role.content"));

        dialog.showAndWait().ifPresent(role -> {
            executeActionCommand(new commands.admin.UpdateRole(String.valueOf(selected.getId()), role.toUpperCase()));
        });
    }

    private void handleCreatePermission() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("admin.dialog.create_permission.title"));
        dialog.setHeaderText(bundle.getString("admin.dialog.create_permission.header"));
        dialog.showAndWait().ifPresent(name -> {
            if(!name.trim().isEmpty()) {
                executeActionCommand(new commands.admin.CreatePermission(name.trim()));
            }
        });
    }

    private void showRoleDetailsDialog(String roleName) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("admin.dialog.role_details.title") + " " + roleName);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        ListView<String> funcsList = new ListView<>();
        if (rolesMap.containsKey(roleName)) {
            funcsList.getItems().addAll(rolesMap.get(roleName));
        }

        Button addBtn = new Button(bundle.getString("admin.dialog.role_details.btn_add"));
        Button delBtn = new Button(bundle.getString("admin.dialog.role_details.btn_delete"));
        addBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
        delBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-cursor: hand;");

        HBox btnBox = new HBox(10, addBtn, delBtn);
        btnBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, new Label(bundle.getString("admin.dialog.role_details.label_permissions")), funcsList, btnBox);
        layout.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(layout);

        addBtn.setOnAction(e -> {
            client.sendCommandAsync(new commands.admin.GetPermissions(), res -> {
                Map<String, String> allPermsMap = (Map<String, String>) res.getObj();

                if (allPermsMap != null) {
                    Platform.runLater(() -> {
                        funcsList.getItems().forEach(allPermsMap::remove);

                        if (allPermsMap.isEmpty()) {
                            logError("admin.error.all_permissions_assigned");
                            return;
                        }

                        Dialog<String> dialogAdd = new Dialog<>();
                        dialogAdd.setTitle(bundle.getString("admin.dialog.select_permission.title"));
                        dialogAdd.setHeaderText(bundle.getString("admin.dialog.select_permission.header") + " " + roleName);

                        ButtonType okButtonType = new ButtonType(bundle.getString("admin.dialog.role_details.btn_add"), ButtonBar.ButtonData.OK_DONE);
                        ButtonType cancelButtonType = new ButtonType(bundle.getString("band.button.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
                        dialogAdd.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

                        ComboBox<String> comboBox = new ComboBox<>();
                        comboBox.getItems().addAll(allPermsMap.keySet());
                        comboBox.getSelectionModel().selectFirst();
                        comboBox.setPrefWidth(250);

                        comboBox.setCellFactory(lv -> new ListCell<String>() {
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setText(null);
                                    setTooltip(null);
                                } else {
                                    setText(item);
                                    Tooltip tooltip = new Tooltip(allPermsMap.get(item));
                                    tooltip.setWrapText(true);
                                    tooltip.setPrefWidth(300);
                                    tooltip.setStyle("-fx-font-size: 13px; -fx-background-color: #34495e;");
                                    setTooltip(tooltip);
                                }
                            }
                        });

                        VBox content = new VBox(10, new Label(bundle.getString("admin.dialog.select_permission.content")), comboBox);
                        dialogAdd.getDialogPane().setContent(content);

                        dialogAdd.setResultConverter(dialogButton -> {
                            if (dialogButton == okButtonType) {
                                return comboBox.getValue();
                            }
                            return null;
                        });

                        dialogAdd.showAndWait().ifPresent(selected -> {
                            client.sendCommandAsync(new commands.admin.AddFunctions(roleName, selected), r -> {
                                Platform.runLater(() -> funcsList.getItems().add(selected));
                                fetchUsersAndRoles();
                                logInfo("admin.info.permission_added::" + selected);
                            }, this::logError);
                        });
                    });
                }
            }, this::logError);
        });

        delBtn.setOnAction(e -> {
            String selected = funcsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                client.sendCommandAsync(new commands.admin.DeleteFunctions(roleName, selected), r -> {
                    Platform.runLater(() -> funcsList.getItems().remove(selected));
                    fetchUsersAndRoles();
                    logInfo("admin.info.permission_deleted::" + selected);
                }, this::logError);
            } else {
                logError("admin.error.cannot_delete_base_role");
            }
        });

        dialog.showAndWait();
    }

    private void executeActionCommand(CommandClient command) {
        client.sendCommandAsync(command, response -> {
            fetchUsersAndRoles();
            if (response.getMessage() != null) logInfo(response.getMessage());
        }, this::logError);
    }

    private void setupI18n() {
        view.languageBox.getItems().addAll("RU / Русский", "NL / Nederlands", "SV / Svenska", "EN / English");

        view.languageBox.setOnAction(e -> {
            String selectedLang = view.languageBox.getValue();
            if (selectedLang != null) {
                changeLanguage(selectedLang);
            }
        });

        String savedLang = windowManager.getCurrentLanguage();
        view.languageBox.setOnAction(null);
        view.languageBox.setValue(savedLang);

        view.languageBox.setOnAction(e -> {
            String selectedLang = view.languageBox.getValue();
            if (selectedLang != null) {
                changeLanguage(selectedLang);
            }
        });

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
        refreshConsole();
    }

    private void updateTexts() {
        view.logoutButton.setText(bundle.getString("main.logout_button"));
        view.searchField.setPromptText(bundle.getString("main.button.search"));
        view.consoleLabel.setText(bundle.getString("admin.console.title"));
        view.tableTab.setText(bundle.getString("admin.table.name_table"));
        view.visualizationTab.setText(bundle.getString("main.table.name_visualization"));

        view.idCol.setText(bundle.getString("main.table.col.id"));
        view.nameCol.setText(bundle.getString("main.table.col.name"));
        view.loginCol.setText(bundle.getString("admin.table.col.login"));
        view.roleCol.setText(bundle.getString("admin.table.col.role"));
        view.statusCol.setText(bundle.getString("admin.table.col.status"));

        view.usernameLabel.setText(bundle.getString("admin.label.admin") + ": ");

        view.blockButton.setText(bundle.getString("admin.button.block"));
        view.unblockButton.setText(bundle.getString("admin.button.unblock"));
        view.updateRoleButton.setText(bundle.getString("admin.button.update_role"));

        view.rolesTitle.setText(bundle.getString("admin.label.roles_title"));
        view.createRoleButton.setText(bundle.getString("admin.button.create_role"));
        view.deleteRoleButton.setText(bundle.getString("admin.button.delete_role"));
        view.createPermissionButton.setText(bundle.getString("admin.button.create_permission"));
        view.deletePermissionButton.setText(bundle.getString("admin.button.delete_permission"));

        currentStage.setTitle(bundle.getString("admin.window.title"));

        view.statusCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().isBanned() ? bundle.getString("admin.status.banned") : bundle.getString("admin.status.active")
        ));
        view.table.refresh();
    }

    private void handleCreateRole() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("admin.dialog.create_role.title"));
        dialog.setHeaderText(bundle.getString("admin.dialog.create_role.header"));
        dialog.setContentText(bundle.getString("admin.dialog.create_role.content"));

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                executeActionCommand(new commands.admin.CreateRole(name.trim().toUpperCase()));
            } else {
                logError("admin.error.empty_role_name");
            }
        });
    }

    private void handleDeletePermission() {
        client.sendCommandAsync(new commands.admin.GetPermissions(), res -> {
            Map<String, String> allPermsMap = (Map<String, String>) res.getObj();

            if (allPermsMap != null && !allPermsMap.isEmpty()) {
                Platform.runLater(() -> {
                    List<String> permsList = new ArrayList<>(allPermsMap.keySet());

                    ChoiceDialog<String> dialog = new ChoiceDialog<>(permsList.get(0), permsList);
                    dialog.setTitle(bundle.getString("admin.dialog.delete_permission.title"));
                    dialog.setHeaderText(bundle.getString("admin.dialog.delete_permission.header"));
                    dialog.setContentText(bundle.getString("admin.dialog.delete_permission.content"));

                    dialog.showAndWait().ifPresent(selected -> {
                        executeActionCommand(new commands.admin.DeletePermission(selected));
                    });
                });
            }
        }, this::logError);
    }

    private void handleDeleteRole() {
        String selectedRole = view.rolesList.getSelectionModel().getSelectedItem();

        if (selectedRole == null) {
            logError("admin.error.select_role_to_delete");
            return;
        }

        if (selectedRole.equals("ADMIN") || selectedRole.equals("USER") ||
                selectedRole.equals("SUPERUSER") || selectedRole.equals("GUEST")) {
            logError(MessageFormat.format(bundle.getString("admin.error.cannot_delete_base_role"), selectedRole));
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.WARNING);
        confirmDialog.setTitle(bundle.getString("admin.dialog.confirm_delete.title"));
        confirmDialog.setHeaderText(bundle.getString("admin.dialog.confirm_delete.header") + " " + selectedRole);
        confirmDialog.setContentText(bundle.getString("admin.dialog.confirm_delete.content"));

        ButtonType yesButton = new ButtonType(bundle.getString("admin.button.yes"), ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType(bundle.getString("admin.button.no"), ButtonBar.ButtonData.NO);
        confirmDialog.getButtonTypes().setAll(yesButton, noButton);

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                executeActionCommand(new commands.admin.DeleteRole(selectedRole));
            }
        });
    }

    private void filterUsers() {
        String query = view.searchField.getText().toLowerCase().trim();
        if (query.isEmpty()) {
            view.table.setItems(allUsers);
            view.visualizationView.setUsers(new ArrayList<>(allUsers));
        } else {
            ObservableList<User> filtered = allUsers.stream()
                    .filter(u -> u.getUserName() != null && u.getUserName().toLowerCase().contains(query) || u.getLogin() != null && u.getLogin().toLowerCase().contains(query))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            view.table.setItems(filtered);
            view.visualizationView.setUsers(new ArrayList<>(filtered));
        }
        view.table.refresh();
    }

    private void logInfo(String msg) {
        LogRecord record = new LogRecord("INFO", msg);
        consoleHistory.add(record);
        appendRecordToUi(record);
    }

    private void logError(String msg) {
        if ("server.auth.error.account_banned".equals(msg)) {
            if (updater != null) updater.stop();
            Platform.runLater(() -> {
                windowManager.showLoginWindow();
            });
            return;
        }

        LogRecord record = new LogRecord("ERROR", msg);
        consoleHistory.add(record);
        appendRecordToUi(record);
    }

    private void appendRecordToUi(LogRecord record) {
        String translated = translateServerMessage(record.rawMessage);
        Platform.runLater(() -> view.consoleLines.add("[" + record.type + "] " + translated));
    }

    private void refreshConsole() {
        Platform.runLater(() -> {
            view.consoleLines.clear();
            for (LogRecord record : consoleHistory) {
                String translated = translateServerMessage(record.rawMessage);
                view.consoleLines.add("[" + record.type + "] " + translated);
            }
        });
    }

    private String translateServerMessage(String serverMsg) {
        if (serverMsg == null || serverMsg.isBlank()) return "";
        String[] parts = serverMsg.split("::");
        String key = parts[0];

        if (bundle.containsKey(key)) {
            if (parts.length > 1) {
                Object[] args = Arrays.copyOfRange(parts, 1, parts.length);
                return MessageFormat.format(bundle.getString(key), args);
            }
            return bundle.getString(key);
        }
        return serverMsg;
    }
}