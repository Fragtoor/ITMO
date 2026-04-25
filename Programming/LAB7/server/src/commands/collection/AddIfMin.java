package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import common.models.MusicBand;
import managers.CollectionManager;

public class AddIfMin extends Command {
    private boolean isAdd;
    private final CollectionManager cm;
    public AddIfMin(CollectionManager cm) {
        this.cm = cm;
    }

    public void undo() {
        if (isAdd) cm.removeById(cm.getMaxId());
    }

    public boolean validateParams(Object... params) {
        return (params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate());
    }

    public Response execute(Object... params) {
        MusicBand band = (MusicBand) params[0];
        boolean result = cm.addIfMin(band);
        isAdd = result;
        cm.addToCommandsList(this);

        if (result) {return new Response(ResponseType.COMMAND_SUCCESS, "Элемент добавлен в коллекцию!\n");}
        return new Response(ResponseType.COMMAND_SUCCESS, "Элемент не добавлен в коллекцию!\n");
    }
    public String getCommandName() {return "add_if_min";}
}
