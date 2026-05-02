package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class FilterContainsName extends Command {
    public FilterContainsName(User user) {
        super(user);
    }

    public boolean validateParams(Object... params) {
        return params.length != 0 && (params[0] instanceof String);
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        String[] result = cm.filterContainsName((String)params[0]);

        try {
            db.saveHistoryCommand(getUser(), this);
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
        } catch (SQLException e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка на стороне сервера при попытке сохранить историю");
        }
    }
    public String getCommandName() {
        return "filter_contains_name";
    }
}
