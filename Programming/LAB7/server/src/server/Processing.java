package server;

import common.net.*;
import common.net.request.*;
import dao.DBManager;
import managers.*;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Processing {

    // Ключ - логин пользователя
    private static final Map<String, TransactionManager> activeTransactions = new ConcurrentHashMap<>();

    private final DBManager db;

    public Processing(DBManager db) {
        this.db = db;
    }

    public Response run(Object request) {
        if (!(request instanceof Request req)) return new Response(ResponseType.COMMAND_ERROR, "Ошибка, неправильный формат запроса");

        User user = req.getUser();

        if (request instanceof AuthRequest req2) return handlerAuthRequest(user, req2);

        // Проверка, есть ли пользователь в БД
        try {
            db.getUserID(user);
        } catch (SQLException e) {
            return new Response(ResponseType.AUTH_ERROR, "Вы не авторизованы");
        }

        if (request instanceof CollectionRequest req3) return handlerCollectionRequest(user, req3);

        if (request instanceof AdminRequest req4) return handlerAdminRequest(user, req4);

        return new Response(ResponseType.COMMAND_ERROR, "Ошибка, неправильный формат запроса");
    }

    private Response handlerCollectionRequest(User user, CollectionRequest req) {
        CollectionManager cm = new CollectionManager();

        // Заполняем данные коллекции пользователя
        try {
            cm.setCollection(db.getAllDataCollection(user));
            cm.setCreationDate(db.getCreationDateCollection());
            cm.setCommandsList(db.getCommandsList(user));
        } catch (Exception e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка обработки данных из БД");
        }

        String commandName = req.getCommandName();
        boolean fromTheFile = req.getFromTheFile();
        String userLogin = user.getLogin();

        if (fromTheFile) {
            // Если транзакции еще нет, создаем и запускаем её
            activeTransactions.computeIfAbsent(userLogin, k -> {
                TransactionManager tm = new TransactionManager();
                tm.beginTransaction();
                return tm;
            });
        } else {
            // прошлый скрипт кончился, удаляем старую транзакцию
            activeTransactions.remove(userLogin);
        }

        try {
            CommandManager commandManager = new CommandManager(cm, db, user);
            Response response = commandManager.executeCollectionCommand(commandName, req.getParams());

            // Успешно выполнили команду скрипта - увеличиваем счетчик
            if (fromTheFile) {
                TransactionManager tm = activeTransactions.get(userLogin);
                if (tm != null) tm.nextCommand();
            }

            return response;

        } catch (Exception e) {
            String result = e.getMessage() + "\n";

            // Если произошла ошибка при выполнении скрипта, то откатываем
            if (fromTheFile) {
                TransactionManager tm = activeTransactions.remove(userLogin);
                if (tm != null) {
                    try {
                        cm.back(tm.rollback(), db);
                    } catch (SQLException ignored) {}

                    result += "Все изменения, вызванные командой execute_script, отменены";
                }
            }
            return new Response(ResponseType.COMMAND_ERROR, result);
        }
    }

    private Response handlerAuthRequest(User user, AuthRequest req) {
        String commandName = req.getCommandName();
        CommandManager commandManager = new CommandManager(db, user);
        return commandManager.executeAuthCommand(commandName);
    }

    private Response handlerAdminRequest(User user, AdminRequest req) {
        String commandName = req.getCommandName();
        CommandManager commandManager = new CommandManager(db, user);
        return commandManager.executeAdminCommand(commandName, req.getParams()); // (заодно тут параметры добавлены, как мы правили ранее)
    }
}