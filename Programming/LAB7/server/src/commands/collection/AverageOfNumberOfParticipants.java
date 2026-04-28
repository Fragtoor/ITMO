package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.BDManager;
import dao.DAO;
import managers.CollectionManager;

import java.sql.SQLException;


public class AverageOfNumberOfParticipants extends Command {
    public AverageOfNumberOfParticipants(User user) {
        super(user);
    }
    public Response execute(CollectionManager cm, DAO dao, Object... params) {
        String message = cm.averageOfNumberOfParticipants();
        try {
            BDManager.saveHistoryCommand(dao, getUser(), this);
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
