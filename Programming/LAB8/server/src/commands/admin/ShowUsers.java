package commands.admin;

import commands.Command;
import common.net.Response;
import common.net.ResponseType;
import common.net.User;
import dao.DBManager;

import java.sql.SQLException;
import java.util.HashMap;

public class ShowUsers extends Command{
    public ShowUsers(User user) {
        super(user);
    }

    public String getCommandName() {
        return "show_users";
    }

    public Response execute(DBManager db, Object... params) {
        try {
            HashMap<String, String[]> permissions =  db.getUsersAndPermissions();
            StringBuilder result = new StringBuilder();
            for (String key: permissions.keySet()) {
                result.append("id: ").append(key).append(") ").append(permissions.get(key)[0]).append(": ").append(permissions.get(key)[1]).append("\n");
            }
            return new Response(ResponseType.COMMAND_SUCCESS, result.toString());
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, e.getMessage());
        }
    }

    public String getRequiredPermission() {
        return "USER_VIEW";
    }
}
