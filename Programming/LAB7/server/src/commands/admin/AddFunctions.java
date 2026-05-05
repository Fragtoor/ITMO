package commands.admin;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.net.Response;
import common.net.ResponseType;
import common.net.User;
import common.tools.Validator;
import dao.DBManager;

import java.sql.SQLException;

public class AddFunctions extends Command{
    public AddFunctions(User user) {
        super(user);
    }
    public void validateParams(Object... params) {
        if (params.length < 2) throw new InvalidInputException("Получено меньше, чем нужно, параметров");
        if (!(params[0] instanceof String n)) throw new InvalidInputException("id - не положительное целое число.");
        if (!Validator.isInt(n) || Integer.parseInt(n) <= 0) throw new InvalidInputException("id - не положительное целое число.");
        for (int i = 1; i < params.length; ++i) {
            if (!(params[i] instanceof String)) throw new InvalidInputException("Не все функциональности переданы в виде строки.");
        }
    }

    public String getCommandName() {
        return "add_functions";
    }

    public Response execute(DBManager db, Object... params) {
        try {
            db.addFunctionsToRole(Integer.parseInt((String)params[0]), params);
            return new Response(ResponseType.COMMAND_SUCCESS, "Функциональности добавлены!");
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, e.getMessage());
        }
    }

    public String getRequiredPermission() {
        return "PERMISSION_MANAGE";
    }
}
