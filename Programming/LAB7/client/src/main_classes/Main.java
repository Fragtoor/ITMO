package main_classes;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.util.Properties;

/**
 * Точка входа в программу.
 */
public class Main {
    /**
     * Запускает программу.
     */
    public static void main(String[] args) {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("properties/application.properties")) {
            props.load(in);
            String host = props.getProperty("server.host");
            int port = Integer.parseInt(props.getProperty("server.port"));
            Client client = new Client(InetAddress.getByName(host), port);
            client.run();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
