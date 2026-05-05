package commands.collection;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class FilterContainsName extends Command {
    public FilterContainsName(User user) {
        super(user);
    }

    public void validateParams(Object... params) {
        if (!(params.length != 0 && (params[0] instanceof String))) {
            throw new InvalidInputException("Получены некорректные или поврежденные данные подстроки.");
        }
    }

    public String getRequiredPermission() {
        return "SEARCH";
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
