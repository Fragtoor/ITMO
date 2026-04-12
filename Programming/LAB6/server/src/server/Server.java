package server;

import tools.CollectionManager;
import tools.FileManager;
import tools.Reader;
import tools.TransactionManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
    private int port;
    private CollectionManager cm;

    public Server(int port, CollectionManager cm) {
        this.port = port;
        this.cm = cm;
    }

    public void run(String fileName) throws IOException {
        try {
            cm.setCollection(FileManager.readCollectionFromFile(fileName));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }


        // Выполняется, когда происходит закрытие сервера
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FileManager.saveCollection(fileName, cm.getCollection());
            System.out.println("Коллекция сохранилась в файл. Закрытие сервера");
        }));

        try (Selector selector = Selector.open();
             ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);

            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Сервер запущен на порту " + port);

            while (true) {

                if (selector.select() == 0) continue;
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    try {
                        if (key.isAcceptable()) {
                            // 2. МОДУЛЬ ПОДКЛЮЧЕНИЯ
                            Acceptor.accept(serverChannel, selector);
                        }
                        if (key.isReadable()) {
                            Object request = Reader.reader(key);
                            if (request != null) {
                                // 3. МОДУЛЬ ОБРАБОТКИ
                                Processing proc = new Processing(cm, fileName);
                                String result = proc.run(request);
                                // 4. МОДУЛЬ ОТПРАВКИ
                                ResponseSender.send((SocketChannel) key.channel(), result);
                            }
                        }
                    } catch (IOException e) {

                        key.cancel();
                        if (key.channel() != null) key.channel().close();
                    } catch (ClassNotFoundException e) {
                        System.err.println("Ошибка десериализации: " + e.getMessage());
                    }
                }
            }
        }
    }
}