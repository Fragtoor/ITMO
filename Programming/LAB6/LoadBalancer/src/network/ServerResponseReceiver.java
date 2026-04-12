package network;

import util.ConnectionContext;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ServerResponseReceiver {

    public static void receive(SelectionKey serverKey) throws IOException {
        SocketChannel serverChannel = (SocketChannel) serverKey.channel();
        ConnectionContext ctx = (ConnectionContext) serverKey.attachment();
        ctx.buffer.clear();
        int bytesRead = serverChannel.read(ctx.buffer);
        if (bytesRead == -1) throw new IOException("Сервер разорвал соединение");

        ctx.buffer.flip();
    }
}
