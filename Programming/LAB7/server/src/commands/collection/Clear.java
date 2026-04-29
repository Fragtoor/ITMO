package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import common.models.MusicBand;
import dao.BDManager;
import dao.DAO;
import managers.CollectionManager;

import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;


public class Clear extends Command {
    public Set<MusicBand> backupCollection;
    public Clear(User user) {
        super(user);
    }

    public void undo(CollectionManager cm, DAO dao) throws SQLException {
        BDManager.addItems(dao, getUser(), backupCollection);
        cm.setCollection(backupCollection);
    }
    public Response execute(CollectionManager cm, DAO dao, Object... params) {
        backupCollection = new ConcurrentSkipListSet<>(cm.getCollection());
        try {
            BDManager.clearCollection(dao, getUser());
            String message = cm.clear();
            BDManager.saveHistoryCommand(dao, getUser(), this);
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
