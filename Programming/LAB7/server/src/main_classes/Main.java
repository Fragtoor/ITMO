package main_classes;

import managers.ServerManagers;
import managers.UserManager;
import server.Server;
import managers.CollectionManager;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("properties/application.properties")) {
            props.load(in);
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

            String fileName = props.getProperty("server.file_name");
            CollectionManager cm = new CollectionManager();
            UserManager um = new UserManager();
            ServerManagers sm = new ServerManagers(cm, um);
            Server server = new Server(port, sm);
            server.run(fileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
