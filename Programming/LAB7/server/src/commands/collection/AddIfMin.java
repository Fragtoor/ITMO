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

public class AddIfMin extends Command {
    MusicBand bandAdd;
    public AddIfMin(User user) {
        super(user);
    }

    public void undo(CollectionManager cm, DAO dao) throws SQLException {
        if (bandAdd != null) {
            BDManager.deleteItem(dao, getUser(), bandAdd.getId());
            cm.removeById(cm.getMaxId());
        }

    }

    public boolean validateParams(Object... params) {
        return (params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate());
    }

    public Response execute(CollectionManager cm, DAO dao, Object... params) {
        MusicBand band = (MusicBand) params[0];
        band.setCreationDate(LocalDateTime.now());
        if (cm.addIfMin(band) == null) {
            return new Response(ResponseType.COMMAND_SUCCESS, "Элемент не добавлен в коллекцию!\n");
        }
        try {
            int id = BDManager.addItem(dao, getUser(), band, -1);
            band.setId(id);
            cm.add(band);

            bandAdd = band;
            BDManager.saveHistoryCommand(dao, getUser(), this);
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, "Элемент добавлен в коллекцию!\n");
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке добавить элемент\n");
        }

    }
    public String getCommandName() {return "add_if_min";}
}
