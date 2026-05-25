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
            boolean canClearAll = db.getUserPermissions(getUser()).contains("CLEAR_ALL");
            String message;

            if (canClearAll) {
                db.clearCollectionAll();
                message = cm.clearAll();
            } else {
                db.clearCollection(getUser());
                message = cm.clear(getUser().getId());
            }

            db.saveHistoryCommand(getUser(), getCommandName());
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).message(message).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("Ошибка при попытке очистить коллекцию\n").build();
        }
    }
    public String getCommandName() {
        return "clear";
    }
}
