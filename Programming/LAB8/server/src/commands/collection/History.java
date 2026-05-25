package commands.collection;

import commands.Command;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;
import java.util.List;


public class History extends Command {
    public History(User user) {
        super(user);
    }

    public String getRequiredPermission() {
        return "VIEW_HISTORY";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        try {
            List<String> lastCommands = db.getLastCommands(getUser(), 10);

            db.saveHistoryCommand(getUser(), getCommandName());
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).obj(lastCommands).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.SERVER_ERROR).message("Ошибка на стороне сервера при чтении истории").build();
        }
    }
    public String getCommandName() {
        return "history";
    }
}
