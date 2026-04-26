package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.BDManager;
import dao.DAO;
import managers.CollectionManager;

import java.sql.SQLException;


public class Show extends Command {
    private final CollectionManager cm;
    public Show(CollectionManager cm, User user, DAO dao) {
        super(user, dao);
        this.cm = cm;
    }

    public Response execute(Object... params) {
        String[] result = cm.show();
        try {
            BDManager.saveHistoryCommand(getDAO(), getUser(), this);
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
        } catch (SQLException e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка на стороне сервера при попытке сохранить историю");
        }

    }
    public String getCommandName() {
        return "show";
    }
}
