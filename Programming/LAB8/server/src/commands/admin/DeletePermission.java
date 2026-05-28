package commands.admin;

import commands.Command;
import common.net.*;
import dao.DBManager;

import java.sql.SQLException;

public class DeletePermission extends Command {
    public DeletePermission(User user) {
        super(user);
    }

    public String getRequiredPermission() {
        return "PERMISSION_MANAGE";
    }

    public String getCommandName() {
        return "delete_permission";
    }

    public Response execute(DBManager db, Object... params) {
        try {
            String permName = ((String) params[0]).trim().toUpperCase();
            db.permissions().deletePermission(permName);
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).message("server.command.delete_permission.success::" + permName).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message(e.getMessage().contains("базовую") ? "server.error.cannot_delete_base_permission" : "server.error.db_error").build();
        }
    }
}