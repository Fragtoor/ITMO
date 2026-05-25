package commands.collection;

import commands.Command;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;

public class SumOfNumberOfParticipants extends Command {
    public SumOfNumberOfParticipants(User user) {
        super(user);
    }

    public String getRequiredPermission() {
        return "READ_STATS";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        String message = cm.sumOfNumberOfParticipants();
        try {
            db.saveHistoryCommand(getUser(), getCommandName());
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).message(message).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.SERVER_ERROR).message("Ошибка на стороне сервера при попытке сохранить историю").build();
        }

    }
    public String getCommandName() {
        return "sum_of_number_of_participants";
    }
}
