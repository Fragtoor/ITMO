package net;

import commands.CommandClient;
import common.net.Response;
import common.net.ResponseType;
import common.net.User;
import common.tools.Reader;
import javafx.application.Platform;
import tools.NetWork;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.function.Consumer;

public class Client {
    private final InetAddress host;
    private final int port;
    private SocketChannel socketChannel;
    private User currentUser;

    public Client(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }


    private SocketChannel connectToServer() throws IOException {
        SocketChannel client = SocketChannel.open();
        client.configureBlocking(true);
        client.connect(new InetSocketAddress(host, port));
        return client;
    }

    public void sendCommandAsync(CommandClient command, Consumer<Response> onSuccess, Consumer<String> onError) {
        command.setUser(this.currentUser);

        new Thread(() -> {
            try {
                Object result;

                synchronized (this) {
                    // Если канал закрылся после прошлой ошибки, открываем его ПЕРЕД отправкой
                    if (socketChannel == null || !socketChannel.isOpen()) {
                        if (socketChannel != null) socketChannel.close();
                        socketChannel = connectToServer();
                    }

                    try {
                        NetWork.sendRequest(socketChannel, command.toRequest());
                        result = Reader.reader(socketChannel);
                    } catch (IOException e) {
                        if (socketChannel != null) socketChannel.close();
                        socketChannel = connectToServer();

                        NetWork.sendRequest(socketChannel, command.toRequest());
                        result = Reader.reader(socketChannel);
                    }
                }

                if (result instanceof Response response) {
                    Platform.runLater(() -> {
                        if (response.getType() == ResponseType.COMMAND_ERROR || response.getType() == ResponseType.AUTH_ERROR) {
                            onError.accept(response.getMessage());
                        } else {
                            onSuccess.accept(response);
                        }
                    });
                }
            } catch (Exception e) {
                // Сервер выключен
                Platform.runLater(() -> onError.accept("NO_CONNECTION"));
            }
        }).start();
    }
}