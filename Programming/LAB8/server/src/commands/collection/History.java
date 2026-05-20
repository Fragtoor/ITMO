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

            if (lastCommands.isEmpty()) {
                db.saveHistoryCommand(getUser(), getCommandName());
                return new Response(ResponseType.COMMAND_SUCCESS, "История команд пуста\n");
            }

            StringBuilder details = new StringBuilder();
            int cnt = 1;
            for (String cmdName : lastCommands) {
                details.append(cnt++).append(") ").append(cmdName).append("\n");
            }

            db.saveHistoryCommand(getUser(), getCommandName());
            return new Response(ResponseType.COMMAND_SUCCESS, "Последние " + lastCommands.size() + " команд:\n", details.toString());
        } catch (SQLException e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка на стороне сервера при чтении истории");
        }
    }
    public String getCommandName() {
        return "history";
    }
}
