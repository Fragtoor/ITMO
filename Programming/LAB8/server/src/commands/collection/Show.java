package commands.collection;

import commands.Command;
import common.models.MusicBand;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;
import java.util.ArrayList;


public class Show extends Command {
    public Show(User user) {
        super(user);
    }

    public String getRequiredPermission() {
        return "READ_COLLECTION";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        ArrayList<MusicBand> collection = cm.show(getUser().getId());
        try {
            db.history().saveHistoryCommand(getUser(), getCommandName());
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).obj(collection).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.SERVER_ERROR).message("server.error.db_error").build();
        }
    }

    public String getCommandName() {
        return "show";
    }
}
