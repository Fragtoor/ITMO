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
    public AddIfMin(User user) {
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
        band.setOwnerId(getUser().getId());
        if (cm.addIfMin(band) == null) {
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).message("server.command.add_if_min.ignored").build();
        }
        try {
            int id = db.addItem(getUser(), band, -1);
            band.setId(id);
            cm.add(band);
            db.saveHistoryCommand(getUser(), getCommandName());
            return new Response.Builder(ResponseType.COMMAND_SUCCESS).message("server.command.add_if_min.success").build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.db_error").build();
        }
    }

    public String getCommandName() {return "add_if_min";}
}
