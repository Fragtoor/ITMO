package commands.admin;
import commands.Command;
import common.net.*;
import dao.DBManager;
import java.sql.SQLException;

public class GetPermissions extends Command {
    public GetPermissions(User user) { super(user); }
    public String getRequiredPermission() { return "ADMIN"; }

    public String getCommandName() { return "get_permissions"; }

    public Response execute(DBManager db, Object... params) {
        try {
            return new Response.Builder(ResponseType.COMMAND_SUCCESS)
                    .obj(db.permissions().getAllPermissionsWithDescription()).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.db_error").build();
        }
    }
}