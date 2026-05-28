package commands.admin;
import commands.Command;
import common.net.*;
import dao.DBManager;
import java.sql.SQLException;

public class CreatePermission extends Command {
    public CreatePermission(User user) { super(user); }
    public String getRequiredPermission() { return "ADMIN"; }

    public String getCommandName() { return "create_permission"; }

    public Response execute(DBManager db, Object... params) {
        try {
            db.permissions().createPermission(((String) params[0]).toUpperCase());
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).message("server.command.create_permission.success").build();
        } catch (SQLException e) { return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.db_error").build(); }
    }
}