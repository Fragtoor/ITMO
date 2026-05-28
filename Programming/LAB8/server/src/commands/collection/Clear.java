package commands.collection;

import commands.Command;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class Clear extends Command {
    public Clear(User user) {
        super(user);
    }

    public String getRequiredPermission() {
        return "CLEAR_OWN";
    }

    public synchronized Response execute(CollectionManager cm, DBManager db, Object... params) {
        try {
            boolean canClearAll = db.permissions().getUserPermissions(getUser()).contains("CLEAR_ALL");
            String message;

            if (canClearAll) {
                db.collection().clearCollectionAll();
                message = cm.clearAll();
            } else {
                db.collection().clearCollection(getUser());
                message = cm.clear(getUser().getId());
            }

            db.history().saveHistoryCommand(getUser(), getCommandName());
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).message(message).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.db_error").build();
        }
    }

    public String getCommandName() {
        return "clear";
    }
}
