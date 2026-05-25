package common.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Reader {
    // Вспомогательный класс для хранения недочитанных данных
    private static class Session {
        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        ByteBuffer dataBuffer = null;
    }

    public static Object reader(SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel channel = (SocketChannel) key.channel();

        Session session = (Session) key.attachment();
        if (session == null) {
            session = new Session();
            key.attach(session);
        }

        // Читаем заголовок (размер сообщения)
        if (session.sizeBuffer.hasRemaining()) {
            int bytesRead = channel.read(session.sizeBuffer);
            if (bytesRead == -1) throw new IOException("Соединение разорвано");

            // Если заголовок еще не дочитан до конца
            if (session.sizeBuffer.hasRemaining()) {
                return null; // Ждем следующего триггера isReadable
            }
        }

        // Если заголовок прочитан полностью, узнаем размер и готовим буфер для тела
        if (session.dataBuffer == null) {
            session.sizeBuffer.flip();
            int dataSize = session.sizeBuffer.getInt();
            session.dataBuffer = ByteBuffer.allocate(dataSize);
        }

        // Читаем само тело объекта
        if (session.dataBuffer.hasRemaining()) {
            int bytesRead = channel.read(session.dataBuffer);
            if (bytesRead == -1) throw new IOException("Соединение разорвано");

            // Если тело пришло не полностью
            if (session.dataBuffer.hasRemaining()) {
                return null; // Ждем следующего триггера isReadable
            }
        }

        // 5. оба буфера заполнены
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(session.dataBuffer.array()))) {
            Object obj = ois.readObject();
            key.attach(null);
            return obj;
        }
    }

    public static Object reader(SocketChannel channel) throws IOException, ClassNotFoundException {
        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        readFull(channel, sizeBuffer);

        sizeBuffer.flip();
        int dataSize = sizeBuffer.getInt();

        ByteBuffer dataBuffer = ByteBuffer.allocate(dataSize);
        readFull(channel, dataBuffer);

        try (ObjectInputStream ois = new ObjectInputStream(new java.io.ByteArrayInputStream(dataBuffer.array()))) {
            return ois.readObject();
        }
    }

    private static void readFull(SocketChannel channel, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            int bytesRead = channel.read(buffer);

            if (bytesRead == -1) {
                throw new IOException("Соединение с сервером разорвано");
            }
        }
    }
}