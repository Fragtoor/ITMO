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
import java.util.LinkedHashSet;
import java.util.stream.Collectors;


public class RemoveGreater extends Command {
    LinkedHashSet<MusicBand> listDelete;
    public RemoveGreater(User user) {
        super(user);
    }

    public void undo(CollectionManager cm, DAO dao) throws SQLException {
        BDManager.addItems(dao, getUser(), listDelete);
        cm.getCollection().addAll(listDelete);
    }

    public boolean validateParams(Object... params) {
        return (params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate());
    }

    public Response execute(CollectionManager cm, DAO dao, Object... params) {
        MusicBand band = (MusicBand) params[0];
        try {
            listDelete = cm.removeGreater(band);

            BDManager.deleteItems(dao, getUser(), listDelete);
            cm.setCollection(cm.getCollection().stream()
                    .filter(elem -> elem.compareTo(band) <= 0)
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
            BDManager.saveHistoryCommand(dao, getUser(), this);
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
