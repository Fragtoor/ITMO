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
    private final CollectionManager cm;
    public RemoveById(CollectionManager cm, User user, DAO dao) {
        super(user, dao);
        this.cm = cm;
    }

    public void undo() throws SQLException {
        if (bandDelete == null) return;
        bandDelete.setId(idDelete);
        BDManager.addItem(getDAO(), getUser(), bandDelete);
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

    public Response execute(Object... params) {
        int numberId = Integer.parseInt((String)params[0]);
        try {
            BDManager.deleteItem(getDAO(), getUser(), numberId);
            BDManager.saveHistoryCommand(getDAO(), getUser(), this);

            MusicBand band = cm.removeById(numberId);
            idDelete = numberId;
            bandDelete = band;
            cm.addToCommandsList(this);
            if (band == null) return new Response(ResponseType.COMMAND_SUCCESS, "Элемента с id " + numberId + " не существует\n");
            return new Response(ResponseType.COMMAND_SUCCESS, "Элемент с id " + numberId + " удалён\n");
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке удалить элемент\n");
        }

    }
    public String getCommandName() {
        return "remove_by_id";
    }
}
