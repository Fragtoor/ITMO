package commands.collection;

import commands.Command;
import common.exceptions.InvalidInputException;
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
    private LinkedHashSet<MusicBand> listDelete;
    private final CollectionManager cm;
    public RemoveGreater(CollectionManager cm, User user, DAO dao) {
        super(user, dao);
        this.cm = cm;
    }

    public void undo() throws SQLException {
        LinkedHashSet<MusicBand> list = listDelete;
        BDManager.addItems(getDAO(), getUser(), list);
        cm.getCollection().addAll(list);
    }

    public boolean validateParams(Object... params) {
        return (params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate());
    }

    public Response execute(Object... params) {
        MusicBand band = (MusicBand) params[0];
        try {
            listDelete = cm.removeGreater(band);
            BDManager.deleteItems(getDAO(), getUser(), listDelete);
            BDManager.saveHistoryCommand(getDAO(), getUser(), this);

            cm.setCollection(cm.getCollection().stream()
                    .filter(elem -> elem.compareTo(band) <= 0)
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
            cm.addToCommandsList(this);
            if (listDelete.isEmpty()) return new Response(ResponseType.COMMAND_SUCCESS, "В коллекции не нашлись объекты, меньшие заданного\n");
            return new Response(ResponseType.COMMAND_SUCCESS, "Из коллекции были удалены элементы, меньшие заданного!\n");
        } catch (InvalidInputException e) {
            return new Response(ResponseType.COMMAND_ERROR, e.getMessage());
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке удалить элементы\n");
        }
    }
    public String getCommandName() {
        return "remove_greater";
    }
}
