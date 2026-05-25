package commands.collection;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.models.MusicBand;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;
import java.util.ArrayList;


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
        try {
            ArrayList<MusicBand> result = cm.filterContainsName((String)params[0], getUser().getId());
            db.saveHistoryCommand(getUser(), getCommandName());
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).obj(result).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.SERVER_ERROR).message("Ошибка на стороне сервера при попытке сохранить историю").build();
        }
    }
    public String getCommandName() {
        return "filter_contains_name";
    }
}
