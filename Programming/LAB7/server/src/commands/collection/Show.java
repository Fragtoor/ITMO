package commands.collection;

import commands.Command;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class Show extends Command {
    public Show(User user) {
        super(user);
    }

    public String getRequiredPermission() {
        return "READ_COLLECTION";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        String[] result = cm.show(getUser().getId());
        try {
            db.saveHistoryCommand(getUser(), getCommandName());
            return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
        } catch (SQLException e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка на стороне сервера при попытке сохранить историю");
        }
    }
    public String getCommandName() {
        return "show";
    }
}
