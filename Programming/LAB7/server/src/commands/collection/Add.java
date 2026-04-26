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
import java.time.LocalDateTime;

public class Add extends Command {
    private final CollectionManager cm;
    MusicBand bandAdd;
    public Add(CollectionManager cm, User user, DAO dao) {
        super(user, dao);
        this.cm = cm;
    }

    public void undo() throws SQLException {
        BDManager.deleteItem(getDAO(), getUser(), bandAdd.getId());
        cm.removeById(bandAdd.getId());
    }

    public boolean validateParams(Object... params) {
        return (params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate());
    }

    public Response execute(Object... params) {
        MusicBand band = (MusicBand) params[0];
        band.setCreationDate(LocalDateTime.now());
        try {
            BDManager.addItem(getDAO(), getUser(), band);
            BDManager.saveHistoryCommand(getDAO(), getUser(), this);

            String message = cm.add(band);
            bandAdd = band;
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, message);

        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке добавить элемент\n");
        }
    }
    public String getCommandName() {
        return "add";
    }
}
