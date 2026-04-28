package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.BDManager;
import dao.DAO;
import managers.CollectionManager;

import java.sql.SQLException;


public class Help extends Command {
    public Help(User user) {
        super(user);
    }

    public Response execute(CollectionManager cm, DAO dao, Object... params) {

        String[] result = cm.help();
        try {
            BDManager.saveHistoryCommand(dao, getUser(), this);
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
        } catch (SQLException e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка на стороне сервера при попытке сохранить историю" + e.getMessage());
        }
    }
    public String getCommandName() {
        return "help";
    }
}
