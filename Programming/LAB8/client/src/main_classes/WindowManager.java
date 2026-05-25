package main_classes;

import common.models.MusicBand;
import common.net.User;
import gui.views.*;
import gui.controllers.*;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.Client;

public class WindowManager {
    private final Stage stage;
    private final Client client;

    public WindowManager(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
    }

    public void showLoginWindow() {
        stage.setMaximized(false);

        LoginView view = new LoginView();
        new LoginController(view, client, this, stage);

        stage.setScene(new Scene(view, 500, 400));
        stage.setResizable(false);

        stage.centerOnScreen();
        stage.show();
    }

    public void showRegisterWindow() {
        RegisterView view = new RegisterView();
        new RegisterController(view, client, this, stage);

        stage.setScene(new Scene(view, 550, 500));
        stage.setResizable(false);
        stage.show();
    }

    public void showMainWindow(User user) {
        MainView view = new MainView();
        new MainController(view, client, this, user, stage);

        stage.setScene(new Scene(view, 800, 600));
        stage.setResizable(true);
        stage.show();
    }

    public void showMusicBandWindow() {
        Stage addStage = new Stage();
        addStage.initModality(Modality.WINDOW_MODAL);
        addStage.initOwner(this.stage);
        addStage.setResizable(false);

        MusicBandView addView = new MusicBandView();
        new MusicBandController(addView, this.client, this, addStage);

        Scene addScene = new Scene(addView);
        addStage.setScene(addScene);
        addStage.showAndWait();
    }

    public void showAddIfMinWindow() {
        Stage addStage = new Stage();
        addStage.initModality(Modality.WINDOW_MODAL);
        addStage.initOwner(this.stage);
        addStage.setResizable(false);

        MusicBandView addView = new MusicBandView();
        new MusicBandController(addView, this.client, this, addStage, MusicBandController.Mode.ADD_IF_MIN);

        addStage.setScene(new Scene(addView));
        addStage.showAndWait();
    }

    public void showRemoveGreaterWindow() {
        Stage addStage = new Stage();
        addStage.initModality(Modality.WINDOW_MODAL);
        addStage.initOwner(this.stage);
        addStage.setResizable(false);

        MusicBandView addView = new MusicBandView();
        new MusicBandController(addView, this.client, this, addStage, MusicBandController.Mode.REMOVE_GREATER);

        addStage.setScene(new Scene(addView));
        addStage.showAndWait();
    }

    public void showUpdateWindowWithBand(MusicBand band) {
        Stage addStage = new Stage();
        addStage.initModality(Modality.WINDOW_MODAL);
        addStage.initOwner(this.stage);
        addStage.setResizable(false);

        MusicBandView addView = new MusicBandView();
        new MusicBandController(addView, this.client, this, addStage, MusicBandController.Mode.UPDATE, band.getId());

        addView.nameField.setText(band.getName());
        addView.participantsField.setText(String.valueOf(band.getNumberOfParticipants()));
        addView.albumsField.setText(band.getAlbumsCount() != null ? String.valueOf(band.getAlbumsCount()) : "");
        addView.salesField.setText(band.getLabel() != null ? String.valueOf(band.getLabel().getSales()) : "");

        if (band.getCoordinates() != null) {
            addView.coordX.setText(String.valueOf(band.getCoordinates().getX()));
            addView.coordY.setText(String.valueOf(band.getCoordinates().getY()));
        }

        if (band.getGenre() != null) {
            addView.genreBox.setValue(band.getGenre().toString());
        }

        if (band.getEstablishmentDate() != null) {
            addView.datePicker.setValue(band.getEstablishmentDate());
        }

        addStage.setScene(new Scene(addView));
        addStage.showAndWait();
    }
}