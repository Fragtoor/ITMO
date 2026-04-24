package network;

import util.ConnectionContext;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ClientResponder {
    public static void respond(SelectionKey clientKey) throws IOException {
        SocketChannel clientChannel = (SocketChannel) clientKey.channel();
        ConnectionContext clientCtx = (ConnectionContext) clientKey.attachment();

        clientChannel.write(clientCtx.buffer);

        if (clientCtx.buffer.hasRemaining()) {
            clientKey.interestOps(clientKey.interestOps() | SelectionKey.OP_WRITE);
        } else {
            clientCtx.buffer.clear();
            clientKey.interestOps(clientKey.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }
}