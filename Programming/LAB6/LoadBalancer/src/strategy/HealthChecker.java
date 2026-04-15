package strategy;

import util.ConnectionContext;
import util.ServerContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HealthChecker {
    private final HashMap<InetSocketAddress, ServerContext> mapStatesServers;
    private final Selector selector;

    public HealthChecker(HashMap<InetSocketAddress, ServerContext> mapStatesServers, Selector selector) {
        this.mapStatesServers = mapStatesServers;
        this.selector = selector;
    }

    public void start() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Properties props = new Properties();
        int initialDelay;
        int period;
        try (FileInputStream in = new FileInputStream("properties/application.properties")) {
            props.load(in);
            initialDelay = Integer.parseInt(props.getProperty("initialDelay"));
            period = Integer.parseInt(props.getProperty("period"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scheduler.scheduleAtFixedRate(this::checkServers, initialDelay, period, TimeUnit.SECONDS);
    }

    private void checkServers() {
        for (ServerContext context : mapStatesServers.values()) {
            InetSocketAddress address = context.getAddress();
            boolean isAlive;

            // Проверка доступности
            try (Socket pingSocket = new Socket()) {
                pingSocket.connect(address, 2000);
                isAlive = true;
            } catch (IOException e) {
                isAlive = false;
            }

            if (isAlive) {
                context.setIsOnline(true);
                if (context.getKey() == null || !context.getKey().channel().isOpen()) {
                    try {
                        SocketChannel serverChannel = SocketChannel.open(address);
                        serverChannel.configureBlocking(false);

                        ConnectionContext serverCtx = new ConnectionContext(ConnectionContext.Type.SERVER);
                        context.setKey(serverChannel.register(selector, SelectionKey.OP_READ, serverCtx));

                    } catch (IOException e) {}
                }


            } else {
                context.setIsOnline(false);

                if (context.getKey() != null) {
                    try {
                        context.getKey().channel().close();
                    } catch (IOException ignored) {}
                    context.getKey().cancel();
                    context.setKey(null);
                }
            }
        }

        printServersStatus();
    }

    private void printServersStatus() {
        System.out.println("=== Статус серверов ===");
        int cnt = 1;
        for (ServerContext context : mapStatesServers.values()) {
            String status = context.getIsOnline() ? "ONLINE" : "OFFLINE";
            System.out.printf("%d) Порт: %d | Статус: %s\n",
                    cnt++, context.getAddress().getPort(), status);
        }
        System.out.println();
    }
}