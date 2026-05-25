package commands.collection;

import commands.Command;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;
import java.util.Locale;


public class AverageOfNumberOfParticipants extends Command {
    public AverageOfNumberOfParticipants(User user) {
        super(user);
    }

    public String getRequiredPermission() {
        return "READ_STATS";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        double avgValue = cm.averageOfNumberOfParticipants();
        try {
            db.saveHistoryCommand(getUser(), getCommandName());
            return new Response.Builder(ResponseType.COMMAND_SUCCESS)
                    .message("server.command.average::" + String.format(Locale.US, "%.2f", avgValue)).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.SERVER_ERROR).message("server.error.db_error").build();
        }
    }

    public String getCommandName() {
        return "average_of_number_of_participants";
    }
}
