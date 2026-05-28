package main_classes;

import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.Client;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.util.Properties;

public class Main extends Application {
    private Client client;

    public void start(Stage primaryStage) {
        initNetwork();
        WindowManager windowManager = new WindowManager(primaryStage, client);
        windowManager.showLoginWindow();
    }

    private void initNetwork() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("properties/application.properties")) {
            props.load(in);
            client = new Client(InetAddress.getByName(props.getProperty("server.host")),
                    Integer.parseInt(props.getProperty("server.port")));
        } catch (Exception e) { System.exit(0); }
    }
}