package commands.collection;

import commands.Command;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class Help extends Command {
    public Help(User user) {
        super(user);
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        try {
            db.history().saveHistoryCommand(getUser(), getCommandName());
            return new Response.Builder(ResponseType.COMMAND_SUCCESS)
                    .message("server.command.help.text").build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.SERVER_ERROR).message("server.error.db_error").build();
        }
    }

    public String getCommandName() {
        return "help";
    }
}
