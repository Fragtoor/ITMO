package server;

import common.general.Response;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ResponseSender {
    public static void send(SocketChannel client, String answer) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Response response = new Response(answer);
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(response);
                oos.flush();
            }
            byte[] data = baos.toByteArray();

            ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
            buffer.putInt(data.length);
            buffer.put(data);
            buffer.flip();
            client.write(buffer);

        } catch (IOException e) {
            System.err.println("Ошибка при отправке ответа: " + e.getMessage());
        }
    }
}
