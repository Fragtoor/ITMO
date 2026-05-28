package server;

import common.net.Response;
import common.tools.Reader;
import dao.DAO;
import dao.DBManager;
import dao.InitDB;
import managers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Server {
    private final int port;
    private final DBManager db;
    private CollectionManager cm;

    public Server(int port) {
        this.port = port;
        db = new DBManager(new DAO());
    }

    public void run() throws Exception {
        // Выполняется, когда происходит закрытие сервера
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Закрытие сервера.");
        }));

        // Migrate sql
        try {
            new InitDB().run("sql/init.sql", db);
        } catch (Exception e) {
            System.out.println("Ошибка при создании таблиц в БД\n" + e.getMessage());
            System.exit(0);
        }

        cm = new CollectionManager(db.collection().getAllDataCollection());
        cm.setCreationDate(db.collection().getCreationDateCollection());

        try (Selector selector = Selector.open();
             ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Сервер запущен на порту " + port);

            handlerEvents(selector, serverChannel);
        }
    }

    private void handlerEvents(Selector selector, ServerSocketChannel serverChannel) throws IOException {
        // Инициализация пулов потоков
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        while (true) {
            if (selector.select() == 0) continue;
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                try {
                    if (key.isAcceptable()) {
                        // МОДУЛЬ ПОДКЛЮЧЕНИЯ
                        Acceptor.accept(serverChannel, selector);
                    }

                    if (key.isReadable()) {
                        key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);

                        // Чтение запроса и его выполнение в отдельном потоке
                        forkJoinPool.submit(() -> readingRequest(key, selector, cachedThreadPool, cm));
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
    private void readingRequest(SelectionKey key, Selector selector, ExecutorService cachedThreadPool, CollectionManager cm) {
        try {
            Object request = Reader.reader(key);

            if (request != null) {
                System.out.println("Пришёл запрос от " + ((SocketChannel) key.channel()).getRemoteAddress());
                new Thread(() -> {
                    Processing proc = new Processing(db, cm);
                    Response response = proc.run(request);

                    cachedThreadPool.submit(() -> {
                        ResponseSender.send((SocketChannel) key.channel(), response);

                        // После успешной отправки возвращаем каналу возможность читать новые запросы
                        if (key.isValid()) {
                            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                            selector.wakeup();
                        }
                    });

                }).start();

            } else {
                if (key.isValid()) {
                    key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                    selector.wakeup();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            key.cancel();
            try {
                if (key.channel() != null) key.channel().close();
            } catch (IOException ignored) {}
        }
    }

}