package commands.collection;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.net.*;
import common.tools.Validator;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class Back extends Command {
    public Back(User user) {
        super(user);
    }

    public void validateParams(Object... params) {
        if (params.length == 0 || !(params[0] instanceof String n)) throw new InvalidInputException("n - не положительное целое число.");
        if (!Validator.isInt(n) || Integer.parseInt(n) <= 0) throw new InvalidInputException("n - не положительное целое число.");
    }

    public String getRequiredPermission() {
        return "CREATE_OBJECT";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        try {
            int n = Integer.parseInt((String)params[0]);
            String message = cm.back(n, db);
            if (n > cm.getCommandsList().size()) {
                return new Response(ResponseType.COMMAND_ERROR, "Было выполнено только " + cm.getCommandsList().size() + " команд");
            }
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
