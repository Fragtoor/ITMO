package strategy;

import util.ServerContext;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChoiceServer {
    private final List<ServerContext> servers;
    private int currentIndex;

    public ChoiceServer(HashMap<InetSocketAddress, ServerContext> mapStatesServers) {
        this.servers = new ArrayList<>(mapStatesServers.values());
        this.currentIndex = 0;
    }

    public ServerContext getActiveServer() {
        if (servers.isEmpty()) return null;
        int size = servers.size();

        for (int i = 0; i < size; i++) {
            currentIndex = (currentIndex + 1) % size;

            ServerContext server = servers.get(currentIndex);
            if (server.getIsOnline()) {
                return server;
            }
        }
        return null;
    }
}