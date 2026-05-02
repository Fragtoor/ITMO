package main_classes;

import server.Server;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try (FileInputStream in = new FileInputStream("properties/application.properties")) {
            new Properties().load(in);
            int port = 0;
            try {
                if (args.length == 0) throw new Exception("Укажите порт");
                port = Integer.parseInt(args[0]);
                if (port < 0 || port > 65535) {
                    throw new Exception("Порт должен быть от 0 до 65535");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }

            Server server = new Server(port);
            server.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
