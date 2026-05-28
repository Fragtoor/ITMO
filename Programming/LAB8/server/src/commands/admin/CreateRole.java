package commands.admin;

import commands.Command;
import common.net.*;
import dao.DBManager;

import java.sql.SQLException;

public class CreateRole extends Command {
    public CreateRole(User user) {
        super(user);
    }

    public String getRequiredPermission() {
        return "ADMIN";
    }

    public String getCommandName() {
        return "create_role";
    }

    public Response execute(DBManager db, Object... params) {
        try {
            String newRole = ((String) params[0]).trim().toUpperCase();
            db.users().createRole(newRole);
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).message("server.command.create_role.success::" + newRole).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR)
                    .message("server.error.db_error").build();
        }
    }
}