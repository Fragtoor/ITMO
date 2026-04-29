package server;

import common.general.Response;
import common.tools.Reader;
import dao.DAO;
import managers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Server {
    private final int port;
    private final ServerManagers sm;
    private final DAO dao;

    public Server(int port, ServerManagers sm) {
        this.port = port;
        this.sm = sm;
        dao = new DAO();
    }

    public void run() throws IOException {
        // Выполняется, когда происходит закрытие сервера
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Закрытие сервера.");
        }));

        sm.userManager.setDAO(dao);

        // Инициализация пулов потоков
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

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
                            // временно отключаем OP_READ для этого клиента, чтобы избежать зацикливания селектора
                            key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);

                            forkJoinPool.submit(() -> {
                                try {
                                    Object request = Reader.reader(key);

                                    if (request != null) {
                                        System.out.println("Пришёл запрос");

                                        new Thread(() -> {
                                            Processing proc = new Processing(sm, dao);
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
                                    } catch (IOException ex) {}
                                }
                            });
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