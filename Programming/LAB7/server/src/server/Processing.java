package server;

import common.net.*;
import common.net.request.*;
import dao.DBManager;
import managers.*;

import java.sql.SQLException;

public class Processing {
    private final DBManager db;

    private final CollectionManager cm;

    public Processing(DBManager db, CollectionManager cm) {
        this.db = db;
        this.cm = cm;
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
        // Заполняем данные коллекции пользователя
        try {
            int userId = db.getUserID(user);
            user.setId(userId);
        } catch (Exception e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка обработки данных из БД");
        }

        String commandName = req.getCommandName();

        try {
            CommandManager commandManager = new CommandManager(cm, db, user);

            return commandManager.executeCollectionCommand(commandName, req.getParams());

        } catch (Exception e) {
            String result = e.getMessage() + "\n";
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
        return commandManager.executeAdminCommand(commandName, req.getParams());
    }
}