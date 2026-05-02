package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class AverageOfNumberOfParticipants extends Command {
    public AverageOfNumberOfParticipants(User user) {
        super(user);
    }
    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        String message = cm.averageOfNumberOfParticipants();
        try {
            db.saveHistoryCommand(getUser(), this);
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, message);
        } catch (SQLException e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка на стороне сервера при попытке сохранить историю");
        }
    }
    public String getCommandName() {
        return "average_of_number_of_participants";
    }
}
