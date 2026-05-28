package commands.admin;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.net.Response;
import common.net.ResponseType;
import common.net.User;
import common.tools.Validator;
import dao.DBManager;

import java.sql.SQLException;

public class UpdateRole extends Command{
    public UpdateRole(User user) {
        super(user);
    }
    public void validateParams(Object... params) {
        if (params.length < 2) throw new InvalidInputException("Получено меньше, чем нужно, параметров");
        if (!(params[0] instanceof String n)) throw new InvalidInputException("id - не положительное целое число.");
        if (!Validator.isInt(n) || Integer.parseInt(n) <= 0) throw new InvalidInputException("id - не положительное целое число.");
        if (!(params[1] instanceof String)) throw new InvalidInputException("role - не строка.");
    }

    public String getCommandName() {
        return "update_role";
    }

    public Response execute(DBManager db, Object... params) {
        try {
            db.users().updateUserRole(Integer.parseInt((String)params[0]), (String)params[1]);
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).message("server.command.update_role.success").build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.db_error").build();
        }
    }

    public String getRequiredPermission() {
        return "ADMIN";
    }
}
