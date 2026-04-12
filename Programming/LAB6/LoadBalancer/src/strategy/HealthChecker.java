package strategy;

import util.ConnectionContext;
import util.ServerContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HealthChecker {
    private final HashMap<InetSocketAddress, ServerContext> mapStatesServers;
    private final Selector selector;
    private final int MAX_COUNT_CONNECTS = 12;

    public HealthChecker(HashMap<InetSocketAddress, ServerContext> mapStatesServers, Selector selector) {
        this.mapStatesServers = mapStatesServers;
        this.selector = selector;
    }

    public void start() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        // Запускаем проверку каждые 4 секунды
        scheduler.scheduleAtFixedRate(this::checkServers, 0, 4, TimeUnit.SECONDS);
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
                if (context.getKey() == null || !context.getKey().channel().isOpen()) {
                    try {
                        SocketChannel serverChannel = SocketChannel.open(address);
                        serverChannel.configureBlocking(false);

                        ConnectionContext serverCtx = new ConnectionContext(ConnectionContext.Type.SERVER);
                        context.setKey(serverChannel.register(selector, SelectionKey.OP_READ, serverCtx));
                        context.setLoadCount(0);

                    } catch (IOException e) {}
                }

                if (context.getLoadCount() >= MAX_COUNT_CONNECTS) {
                    context.setIsOnline(false);
                } else {
                    context.setIsOnline(true);
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
            System.out.printf("%d) Порт: %d | Статус: %s | Нагрузка: %d/%d\n",
                    cnt++, context.getAddress().getPort(), status, context.getLoadCount(), MAX_COUNT_CONNECTS);
        }
        System.out.println();
    }
}