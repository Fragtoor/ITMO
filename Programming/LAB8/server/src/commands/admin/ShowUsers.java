package commands.admin;

import commands.Command;
import common.net.Response;
import common.net.ResponseType;
import common.net.User;
import dao.DBManager;

import java.sql.SQLException;
import java.util.ArrayList;

public class ShowUsers extends Command{
    public ShowUsers(User user) {
        super(user);
    }

    public String getCommandName() {
        return "show_users";
    }

    public Response execute(DBManager db, Object... params) {
        try {
            ArrayList<User> usersList = db.users().getAllUsersList();
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).obj(usersList).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.db_error").build();
        }
    }

    public String getRequiredPermission() {
        return "ADMIN";
    }
}