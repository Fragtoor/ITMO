package server;

import common.general.*;
import dao.DBManager;
import managers.*;

import java.sql.SQLException;

public class Processing {
    private TransactionManager tm = null;
    private final DBManager db;
    public Processing(DBManager db) {
        this.db = db;
    }

    public Response run(Object request) {
        if (!(request instanceof Request req)) return new Response(ResponseType.COMMAND_ERROR, "Ошибка, неправильный формат запроса");

        User user = req.getUser();

        if (request instanceof AuthRequest) {
            String commandName = req.getCommandName();
            CommandManager commandManager = new CommandManager(db, user);
            return commandManager.executeAuthCommand(commandName);
        }

        // Проверка, есть ли пользователь в БД
        try {
            db.getUserID(user);
        } catch (SQLException e) {
            return new Response(ResponseType.AUTH_ERROR, "Вы не авторизованы");
        }

        if (request instanceof CollectionRequest<?, ?> req2) {
            return handlerCollectionRequest(user, req2);
        }

        return new Response(ResponseType.COMMAND_ERROR, "Ошибка, неправильный формат запроса");
    }

    private Response handlerCollectionRequest(User user, CollectionRequest<?, ?> req) {
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
        var argumentParam = req.getArgumentParam();
        var argumentObject = req.getArgumentObject();
        boolean fromTheFile = req.getFromTheFile();

        if (fromTheFile && tm == null) {
            tm = new TransactionManager();
            tm.beginTransaction();
        }

        if (!fromTheFile && tm != null) {tm = null;}
        try {
            CommandManager commandManager = new CommandManager(cm, db, user);
            Response response = commandManager.executeCollectionCommand(commandName, argumentParam, argumentObject);

            if (tm != null) tm.nextCommand();
            return response;
        } catch (Exception e) {
            String result = e.getMessage() + "\n";
            if (tm != null) {
                try {
                    cm.back(tm.rollback(), db);
                } catch (SQLException ignored) {}

                result += "Все изменения, вызванные командой execute_script, отменены";
                tm = null;
            }
            return new Response(ResponseType.COMMAND_ERROR, result);
        }
    }
}
