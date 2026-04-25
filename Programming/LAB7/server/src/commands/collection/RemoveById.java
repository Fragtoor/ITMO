package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.models.MusicBand;
import managers.CollectionManager;


public class RemoveById extends Command {
    private int idDelete;
    private MusicBand bandDelete;
    private final CollectionManager cm;
    public RemoveById(CollectionManager cm) {
        this.cm = cm;
    }

    public void undo() {
        if (bandDelete == null) return;
        bandDelete.setId(idDelete);
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
        MusicBand band = cm.removeById(numberId);
        idDelete = numberId;
        bandDelete = band;
        cm.addToCommandsList(this);
        if (band == null) return new Response(ResponseType.COMMAND_SUCCESS, "Элемента с id " + numberId + " не существует\n");
        return new Response(ResponseType.COMMAND_SUCCESS, "Элемент с id " + numberId + " удалён\n");
    }
    public String getCommandName() {
        return "remove_by_id";
    }
}
