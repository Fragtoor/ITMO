package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class Back extends Command {
    public Back(User user) {
        super(user);
    }

    public boolean validateParams(Object... params) {
        if (params.length == 0 || !(params[0] instanceof String n)) return false;
        int number;
        try {
            number = Integer.parseInt(n);
            if (number < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        try {
            int n = Integer.parseInt((String)params[0]);
            String message = cm.back(n, db);
            for (int i = 0; i < n; i++) {
                db.deleteHistoryCommand(getUser());
                cm.getCommandsList().pop();
            }
            return new Response(ResponseType.COMMAND_SUCCESS, message);
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке отменить команды\n");
        }
    }
    public String getCommandName() {
        return "back";
    }
}
