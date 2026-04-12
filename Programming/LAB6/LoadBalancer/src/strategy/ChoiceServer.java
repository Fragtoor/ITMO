package strategy;

import util.ServerContext;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class ChoiceServer {
    private HashMap<InetSocketAddress, ServerContext> mapStatesServers;

    public ChoiceServer(HashMap<InetSocketAddress, ServerContext> mapStatesServers) {
        this.mapStatesServers = mapStatesServers;
    }

    public ServerContext getActiveServer() {
        if (mapStatesServers.isEmpty()) return null;
        for (var server: mapStatesServers.keySet()) {
            if (mapStatesServers.get(server).getIsOnline()) return mapStatesServers.get(server);
        }
        return null;
    }
}
