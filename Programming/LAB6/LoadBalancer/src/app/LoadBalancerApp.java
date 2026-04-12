package app;


import strategy.ChoiceServer;
import strategy.HealthChecker;
import util.*;
import network.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

public class LoadBalancerApp {
    private final int port;

    private HashMap<InetSocketAddress, ServerContext> mapStatesServers;
    private ServerSocketChannel balancerChannel;
    private Selector selector;

    ChoiceServer choiceServer;

    public LoadBalancerApp(ArrayList<InetSocketAddress> servers, int port) {
        mapStatesServers = new HashMap<>();
        for (var address: servers) {
            mapStatesServers.put(address, new ServerContext(address));
        }
        this.port = port;
        choiceServer = new ChoiceServer(mapStatesServers);
    }

    public void run() throws IOException {
        try (Selector selector = Selector.open();
             ServerSocketChannel balancerChannel = ServerSocketChannel.open()) {
            this.balancerChannel = balancerChannel;
            this.selector = selector;
            balancerChannel.bind(new InetSocketAddress(port));
            balancerChannel.configureBlocking(false);

            balancerChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Балансер запущен на порту " + port);

            workWithClientServer();
        }
    }

    private void workWithClientServer() throws IOException {
        HealthChecker healthChecker = new HealthChecker(mapStatesServers, selector);
        healthChecker.start();
        while (selector.isOpen()) {
            if (selector.select() == 0) continue;
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                if (!key.isValid()) continue;

                try {
                    if (key.isAcceptable()) ClientAcceptor.accept(balancerChannel, selector);

                    if (key.isReadable() || key.isWritable()) {
                        handleOperation(key);
                    }
                } catch (IOException e) {
                    key.cancel();
                    if (key.channel() != null) {
                        try {
                            key.channel().close();
                        } catch (IOException ex) {
                        }
                    }
                    for (var serverAddress : mapStatesServers.keySet()) {
                        ServerContext context = mapStatesServers.get(serverAddress);
                        if (context.getKey() == key) {
                            context.setKey(null);
                            context.setIsOnline(false);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void handleOperation(SelectionKey key) throws IOException {
        ConnectionContext ctx = (ConnectionContext) key.attachment();

        if (key.isWritable()) {
            if (ctx.getType() == ConnectionContext.Type.CLIENT) {
                ClientResponder.respond(key);
            }
            return;
        }

        if (key.isReadable()) {
            if (ctx.getType() == ConnectionContext.Type.CLIENT) {
                RequestForwarder.forward(key, choiceServer.getActiveServer());
            } else {
                ServerResponseReceiver.receive(key);

                ConnectionContext clientCtx = (ConnectionContext) ctx.partnerKey.attachment();

                clientCtx.buffer.clear();
                clientCtx.buffer.put(ctx.buffer);
                clientCtx.buffer.flip();

                ClientResponder.respond(ctx.partnerKey);
            }
        }
    }
}