package commands.collection;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.net.*;
import common.models.MusicBand;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class AddIfMin extends Command {
    MusicBand bandAdd;
    public AddIfMin(User user) {
        super(user);
    }

    public void undo(CollectionManager cm, DBManager db) throws SQLException {
        if (bandAdd != null) {
            db.deleteItem(bandAdd.getId());
            cm.removeById(bandAdd.getId());
        }
    }

    public String getRequiredPermission() {
        return "CREATE_OBJECT";
    }

    public void validateParams(Object... params) {
        if (!((params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate()))) {
            throw new InvalidInputException("Получены некорректные или поврежденные данные объекта MusicBand.");
        }
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        MusicBand band = (MusicBand) params[0];
        band.setCreationDate(LocalDateTime.now());
        if (cm.addIfMin(band) == null) {
            return new Response(ResponseType.COMMAND_SUCCESS, "Элемент не добавлен в коллекцию\n");
        }
        try {
            int id = db.addItem(getUser(), band, -1);
            band.setId(id);
            cm.add(band);

            bandAdd = band;
            db.saveHistoryCommand(getUser(), this);
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, "Элемент добавлен в коллекцию!\n");
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке добавить элемент\n");
        }

    }
    public String getCommandName() {return "add_if_min";}
}
