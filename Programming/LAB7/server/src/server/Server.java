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

public class Server {
    private final int port;
    private final ServerManagers sm;
    private final DAO dao;

    public Server(int port, ServerManagers sm) {
        this.port = port;
        this.sm = sm;
        dao = new DAO();
    }

    public void run(String fileName) throws IOException {
        // Выполняется, когда происходит закрытие сервера
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Закрытие сервера.");
        }));

        sm.userManager.setDAO(dao);

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
                                System.out.println("Пришёл запрос");
                                // 3. МОДУЛЬ ОБРАБОТКИ
                                Processing proc = new Processing(sm, dao, fileName);
                                Response response = proc.run(request);
                                // 4. МОДУЛЬ ОТПРАВКИ
                                ResponseSender.send((SocketChannel) key.channel(), response);
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