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


public class RemoveById extends Command {
    private int idDelete;
    private MusicBand bandDelete;
    public RemoveById(User user) {
        super(user);
    }

    public void undo(CollectionManager cm, DAO dao) throws SQLException {
        if (bandDelete == null) return;
        bandDelete.setId(idDelete);
        BDManager.addItem(dao, getUser(), bandDelete, idDelete);
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

    public Response execute(CollectionManager cm, DAO dao, Object... params) {
        int numberId = Integer.parseInt((String)params[0]);
        try {
            BDManager.deleteItem(dao, getUser(), numberId);
            MusicBand band = cm.removeById(numberId);
            idDelete = numberId;
            bandDelete = band;
            BDManager.saveHistoryCommand(dao, getUser(), this);
            cm.addToCommandsList(this);
            if (band == null) return new Response(ResponseType.COMMAND_SUCCESS, "Элемента с id " + numberId + " не существует");
            return new Response(ResponseType.COMMAND_SUCCESS, "Элемент с id " + numberId + " удалён");
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке удалить элемент");
        }

    }
    public String getCommandName() {
        return "remove_by_id";
    }
}
