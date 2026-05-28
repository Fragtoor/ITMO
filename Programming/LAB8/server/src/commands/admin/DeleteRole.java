package commands.admin;

import commands.Command;
import common.net.*;
import dao.DBManager;

import java.sql.SQLException;

public class DeleteRole extends Command {
    public DeleteRole(User user) {
        super(user);
    }

    public String getRequiredPermission() {
        return "ADMIN";
    }

    public String getCommandName() {
        return "delete_role";
    }

    public Response execute(DBManager db, Object... params) {
        try {
            String roleName = ((String) params[0]).trim().toUpperCase();

            if (roleName.equals("ADMIN") || roleName.equals("USER") ||
                    roleName.equals("SUPERUSER") || roleName.equals("GUEST")) {
                return new Response.Builder(ResponseType.COMMAND_ERROR)
                        .message("server.error.cannot_delete_base_role::" + roleName).build();
            }

            db.users().deleteRole(roleName);
            return new Response.Builder(ResponseType.COMMAND_SUCCESS)
                    .message("server.command.delete_role.success::" + roleName).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR)
                    .message("server.error.db_error").build();
        }
    }
}