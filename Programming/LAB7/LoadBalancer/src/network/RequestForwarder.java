package network;

import general.Response;
import util.ConnectionContext;
import util.ServerContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public class RequestForwarder {
    public static void forward(SelectionKey clientKey, ServerContext server) throws IOException {
        SocketChannel clientChannel = (SocketChannel) clientKey.channel();
        ConnectionContext clientCtx = (ConnectionContext) clientKey.attachment();

        int read = clientChannel.read(clientCtx.buffer);
        if (read == -1) throw new IOException("Разорвано соединение");
        if (read == 0) return;

        if (server == null) {
            String message = "Нет активных серверов.\n";

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(new Response(message));
                oos.flush();
            }
            byte[] data = baos.toByteArray();

            ByteBuffer dataBuffer = ByteBuffer.allocate(4 + data.length);
            dataBuffer.putInt(data.length);
            dataBuffer.put(data);

            dataBuffer.flip();
            ((ConnectionContext)clientKey.attachment()).buffer.clear();
            ((ConnectionContext)clientKey.attachment()).buffer.put(dataBuffer);
            ((ConnectionContext)clientKey.attachment()).buffer.flip();
            ClientResponder.respond(clientKey);

            clientCtx.buffer.clear();
            return;
        }
        clientCtx.partnerKey = server.getKey();
        ((ConnectionContext) server.getKey().attachment()).partnerKey = clientKey;

        clientCtx.buffer.flip();

        SocketChannel serverChannel = (SocketChannel) clientCtx.partnerKey.channel();
        serverChannel.write(clientCtx.buffer);

        if (!clientCtx.buffer.hasRemaining()) {
            clientCtx.buffer.clear();
        }
    }
}
