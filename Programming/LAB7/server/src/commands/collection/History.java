package commands.collection;

import commands.Command;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class History extends Command {
    public History(User user) {
        super(user);
    }

    public String getRequiredPermission() {
        return "VIEW_HISTORY";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        cm.addToCommandsList(this);
        String[] result = cm.history();
        try {
            db.saveHistoryCommand(getUser(), this);
            return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
        } catch (SQLException e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка на стороне сервера при попытке сохранить историю");
        }
    }
    public String getCommandName() {
        return "history";
    }
}
