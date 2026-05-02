package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import common.models.MusicBand;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;


public class Clear extends Command {
    public Set<MusicBand> backupCollection;
    public Clear(User user) {
        super(user);
    }

    public void undo(CollectionManager cm, DBManager db) throws SQLException {
        db.addItems(getUser(), backupCollection);
        cm.setCollection(backupCollection);
    }
    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        backupCollection = new ConcurrentSkipListSet<>(cm.getCollection());
        try {
            db.clearCollection(getUser());
            String message = cm.clear();
            db.saveHistoryCommand(getUser(), this);
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, message);
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке очистить коллекцию\n");
        }

    }
    public String getCommandName() {
        return "clear";
    }
}
