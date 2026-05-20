package server;

import common.net.Response;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ResponseSender {
    public static void send(SocketChannel client, Response response) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(response);
                oos.flush();
            }
            byte[] data = baos.toByteArray();
            ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
            buffer.putInt(data.length);
            buffer.put(data);
            buffer.flip();

            while (buffer.hasRemaining()) {
                int bytesWritten = client.write(buffer);

                // Если буфер переполнен
                if (bytesWritten == 0) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при отправке ответа: " + e.getMessage());
        }
    }
}