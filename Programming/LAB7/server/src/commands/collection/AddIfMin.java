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
    private boolean isAdd;
    private final CollectionManager cm;
    MusicBand bandAdd;
    public AddIfMin(CollectionManager cm, User user, DAO dao) {
        super(user, dao);
        this.cm = cm;
    }

    public void undo() throws SQLException {
        if (isAdd) {
            BDManager.deleteItem(getDAO(), getUser(), bandAdd.getId());
            cm.removeById(cm.getMaxId());
        }

    }

    public boolean validateParams(Object... params) {
        return (params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate());
    }

    public Response execute(Object... params) {
        MusicBand band = (MusicBand) params[0];
        band.setCreationDate(LocalDateTime.now());
        if (cm.addIfMin(band) == null) {
            return new Response(ResponseType.COMMAND_SUCCESS, "Элемент не добавлен в коллекцию!\n");
        }
        isAdd = true;
        bandAdd = band;
        cm.addToCommandsList(this);
        try {
            BDManager.addItem(getDAO(), getUser(), band);
            BDManager.saveHistoryCommand(getDAO(), getUser(), this);

            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, "Элемент добавлен в коллекцию!\n");
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке добавить элемент\n");
        }

    }
    public String getCommandName() {return "add_if_min";}
}
