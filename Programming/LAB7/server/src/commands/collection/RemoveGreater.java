package commands.collection;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.net.*;
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
        db.restoreItems(listDelete);
        cm.addAll(listDelete);
    }

    public void validateParams(Object... params) {
        if (!((params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate()))) {
            throw new InvalidInputException("Получены некорректные или поврежденные данные объекта MusicBand.");
        }
    }

    public String getRequiredPermission() {
        return "DELETE_OWN";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        MusicBand band = (MusicBand) params[0];
        try {
            listDelete = cm.removeGreater(band);
            boolean canDeleteAll = db.getUserPermissions(getUser()).contains("DELETE_ALL");
            if (!canDeleteAll) {
                listDelete = listDelete.stream().filter(MusicBand::isOwner).collect(Collectors.toCollection(ConcurrentSkipListSet::new));
            }
            db.deleteItems(listDelete);
            cm.removeAll(listDelete);
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
