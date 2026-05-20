package commands.collection;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.net.*;
import common.models.MusicBand;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Add extends Command {
    public Add(User user) {
        super(user);
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
        try {
            int id = db.addItem(getUser(), band, -1);
            band.setId(id);
            String message = cm.add(band);
            db.saveHistoryCommand(getUser(), getCommandName());
            return new Response(ResponseType.COMMAND_SUCCESS, message);

        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке добавить элемент\n");
        }
    }
    public String getCommandName() {
        return "add";
    }
}
