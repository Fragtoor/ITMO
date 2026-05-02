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
import java.util.stream.Collectors;


public class RemoveGreater extends Command {
    Set<MusicBand> listDelete;
    public RemoveGreater(User user) {
        super(user);
    }

    public void undo(CollectionManager cm, DBManager db) throws SQLException {
        db.addItems(getUser(), listDelete);
        cm.getCollection().addAll(listDelete);
    }

    public boolean validateParams(Object... params) {
        return (params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate());
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        MusicBand band = (MusicBand) params[0];
        try {
            listDelete = cm.removeGreater(band);

            db.deleteItems(getUser(), listDelete);
            cm.setCollection(cm.getCollection().stream()
                    .filter(elem -> elem.compareTo(band) <= 0)
                    .collect(Collectors.toCollection(ConcurrentSkipListSet::new)));
            db.saveHistoryCommand(getUser(), this);
            cm.addToCommandsList(this);
            if (listDelete.isEmpty()) return new Response(ResponseType.COMMAND_SUCCESS, "В коллекции не нашлись объекты, превышающие заданного\n");
            return new Response(ResponseType.COMMAND_SUCCESS, "Из коллекции были удалены элементы, превышающие заданного!\n");
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке удалить элементы\n");
        }
    }
    public String getCommandName() {
        return "remove_greater";
    }
}
