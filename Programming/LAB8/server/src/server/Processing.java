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
        if (!(request instanceof Request req))
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.invalid_request").build();
        User user = req.getUser();

        if (request instanceof AuthRequest req2) return handlerAuthRequest(user, req2);
        try {
            user.setId(db.users().getUserID(user));
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.AUTH_ERROR).message("server.error.unauthorized").build();
        }

        try {
            if (db.users().isUserBanned(((Request)request).getUser().getId())) {
                return new Response.Builder(ResponseType.AUTH_ERROR)
                        .message("server.auth.error.account_banned")
                        .build();
            }
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.invalid_request").build();
        }


        if (request instanceof CollectionRequest req3) return handlerCollectionRequest(user, req3);
        if (request instanceof AdminRequest req4) return handlerAdminRequest(user, req4);

        return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.invalid_request").build();
    }

    private Response handlerCollectionRequest(User user, CollectionRequest req) {
        // Заполняем данные коллекции пользователя
        try {
            int userId = db.users().getUserID(user);
            user.setId(userId);
        } catch (Exception e) {
            return new Response.Builder(ResponseType.SERVER_ERROR).message("server.error.db_process").build();
        }

        String commandName = req.getCommandName();

        try {
            CommandManager commandManager = new CommandManager(cm, db, user);

            return commandManager.executeCollectionCommand(commandName, req.getParams());

        } catch (Exception e) {
            String result = e.getMessage() + "\n";
            return new Response.Builder(ResponseType.COMMAND_ERROR).message(result).build();
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