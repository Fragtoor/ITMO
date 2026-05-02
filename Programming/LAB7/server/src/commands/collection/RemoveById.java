package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import common.models.MusicBand;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class RemoveById extends Command {
    private int idDelete;
    private MusicBand bandDelete;
    public RemoveById(User user) {
        super(user);
    }

    public void undo(CollectionManager cm, DBManager db) throws SQLException {
        if (bandDelete == null) return;
        bandDelete.setId(idDelete);
        db.addItem(getUser(), bandDelete, idDelete);
        cm.getCollection().add(bandDelete);
    }

    public boolean validateParams(Object... params) {
        if (params.length == 0 || !(params[0] instanceof String)) {
            return false;
        }
        int id;
        try {
            id = Integer.parseInt((String)params[0]);
            if (id <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {return false;}
        return true;
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        int numberId = Integer.parseInt((String)params[0]);
        try {
            MusicBand band = cm.getBand(numberId);
            if (band == null) {
                return new Response(ResponseType.COMMAND_SUCCESS, "Элемента с id " + numberId + " не существует");
            }

            if (band.isOwner()) {
                db.deleteItem(getUser(), numberId);
                cm.removeById(numberId);
                idDelete = numberId;
                bandDelete = band;
                db.saveHistoryCommand(getUser(), this);
                cm.addToCommandsList(this);
                return new Response(ResponseType.COMMAND_SUCCESS, "Элемент с id " + numberId + " удалён");
            }
            db.saveHistoryCommand(getUser(), this);
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_ERROR, "Элемент с id " + numberId + " создан не вами");

        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке удалить элемент");
        }
    }
    public String getCommandName() {
        return "remove_by_id";
    }
}
