package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;

public class Info extends Command {
    public Info(User user) {
        super(user);
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        String[] result = cm.info();

        try {
            db.saveHistoryCommand(getUser(), this);
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
        } catch (SQLException e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка на стороне сервера при попытке сохранить историю");
        }
    }
    public String getCommandName() {
        return "info";
    }
}
