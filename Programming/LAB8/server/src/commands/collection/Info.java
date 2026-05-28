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
        String result = cm.info();
        try {
            db.history().saveHistoryCommand(getUser(), getCommandName());
            return new Response.Builder(ResponseType.COMMAND_SUCCESS)
                    .message("server.command.info.success::" + result).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.SERVER_ERROR).message("server.error.db_error").build();
        }
    }

    public String getCommandName() {
        return "info";
    }
}
