package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.BDManager;
import dao.DAO;
import managers.CollectionManager;

import java.sql.SQLException;


public class FilterContainsName extends Command {
    private final CollectionManager cm;
    public FilterContainsName(CollectionManager cm, User user, DAO dao) {
        super(user, dao);
        this.cm = cm;
    }

    public boolean validateParams(Object... params) {
        return params.length != 0 && (params[0] instanceof String);
    }

    public Response execute(Object... params) {
        String[] result = cm.filterContainsName((String)params[0]);

        try {
            BDManager.saveHistoryCommand(getDAO(), getUser(), this);
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
