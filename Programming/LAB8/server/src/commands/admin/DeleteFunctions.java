package commands.admin;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.net.Response;
import common.net.ResponseType;
import common.net.User;
import dao.DBManager;

import java.sql.SQLException;
import java.util.Arrays;

public class DeleteFunctions extends Command{
    public DeleteFunctions(User user) {
        super(user);
    }
    public void validateParams(Object... params) {
        if (params.length < 2) throw new InvalidInputException("Получено меньше, чем нужно, параметров");
        if (!(params[0] instanceof String)) throw new InvalidInputException("название роли - не строка.");
        for (int i = 1; i < params.length; ++i) {
            if (!(params[i] instanceof String)) throw new InvalidInputException("Не все функциональности переданы в виде строки.");
        }
    }

    public String getCommandName() {
        return "delete_functions";
    }

    public Response execute(DBManager db, Object... params) {
        try {
            db.users().deleteFunctionsToRole((String)params[0], Arrays.copyOfRange(params, 1, params.length));
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).message("server.command.delete_functions.success").build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.db_error").build();
        }
    }

    public String getRequiredPermission() {
        return "ADMIN";
    }
}
