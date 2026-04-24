package network;

import util.ConnectionContext;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ClientAcceptor {
    public static void accept(ServerSocketChannel balancerChannel, Selector selector) throws IOException {
        SocketChannel clientChannel = balancerChannel.accept();
        clientChannel.configureBlocking(false);
        ConnectionContext clientCtx = new ConnectionContext(ConnectionContext.Type.CLIENT);
        clientChannel.register(selector, SelectionKey.OP_READ, clientCtx);
    }
}
