package commands.collection;

import commands.Command;
import common.net.*;
import common.models.MusicBand;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;


public class Clear extends Command {
    public Set<MusicBand> backupCollection;

    public Clear(User user) {
        super(user);
    }

    public void undo(CollectionManager cm, DBManager db) throws SQLException {
        db.restoreItems(backupCollection);
        cm.setCollection(backupCollection);
    }

    public String getRequiredPermission() {
        return "CLEAR_OWN";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        try {
            boolean canClearAll = db.getUserPermissions(getUser()).contains("CLEAR_ALL");
            String message;

            if (canClearAll) {
                backupCollection = new ConcurrentSkipListSet<>(cm.getCollection());
                db.clearCollectionAll();
                message = cm.clearAll();
            } else {
                backupCollection = cm.getCollection().stream()
                        .filter(MusicBand::isOwner)
                        .collect(Collectors.toCollection(ConcurrentSkipListSet::new));
                db.clearCollection(getUser());
                message = cm.clear();
            }

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
