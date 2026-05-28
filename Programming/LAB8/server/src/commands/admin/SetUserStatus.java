package commands.admin;
import commands.Command;
import common.net.*;
import dao.DBManager;
import java.sql.SQLException;

public class SetUserStatus extends Command {
    public SetUserStatus(User user) { super(user); }
    public String getRequiredPermission() { return "ADMIN"; }
    public String getCommandName() { return "set_user_status"; }

    public Response execute(DBManager db, Object... params) {
        try {
            int userId = Integer.parseInt((String) params[0]);
            boolean isBanned = Boolean.parseBoolean((String) params[1]);
            if (userId == db.users().getUserID(getUser())) return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.cannot_change_own_status").build();
            db.users().setUserBanned(userId, isBanned);
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).message(isBanned ? "server.command.user_banned" : "server.command.user_unbanned").build();

        } catch (SQLException e) { return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.db_error").build(); }
    }
}