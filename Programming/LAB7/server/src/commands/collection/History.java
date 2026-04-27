package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.BDManager;
import dao.DAO;
import managers.CollectionManager;

import java.sql.SQLException;


public class History extends Command {
    public History(User user) {
        super(user);
    }

    public Response execute(CollectionManager cm, DAO dao, Object... params) {
        cm.addToCommandsList(this);
        String[] result = cm.history();
        try {
            BDManager.saveHistoryCommand(dao, getUser(), this);
            return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
        } catch (SQLException e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка на стороне сервера при попытке сохранить историю");
        }
    }
    public String getCommandName() {
        return "history";
    }
}
