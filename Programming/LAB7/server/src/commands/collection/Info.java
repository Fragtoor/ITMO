package commands.collection;

import commands.Command;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;

public class Info extends Command {
    public Info(User user) {
        super(user);
    }

    public String getRequiredPermission() {
        return "READ_INFO";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        String[] result = cm.info();

        try {
            db.saveHistoryCommand(getUser(), getCommandName());
            return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
        } catch (SQLException e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка на стороне сервера при попытке сохранить историю");
        }
    }
    public String getCommandName() {
        return "info";
    }
}
