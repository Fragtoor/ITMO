package commands.collection;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.general.Response;
import common.general.ResponseType;
import common.models.MusicBand;
import managers.CollectionManager;

import java.util.LinkedHashSet;


public class RemoveGreater extends Command {
    private LinkedHashSet<MusicBand> listDelete;
    private final CollectionManager cm;
    public RemoveGreater(CollectionManager cm) {
        this.cm = cm;
    }

    public void undo() {
        LinkedHashSet< MusicBand > list = listDelete;
        cm.getCollection().addAll(list);
    }

    public boolean validateParams(Object... params) {
        return (params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate());
    }

    public Response execute(Object... params) {
        MusicBand band = (MusicBand) params[0];
        try {
            listDelete = cm.removeGreater(band);
            cm.addToCommandsList(this);
            if (listDelete.isEmpty()) return new Response(ResponseType.COMMAND_SUCCESS, "В коллекции не нашлись объекты, меньшие заданного\n");
            return new Response(ResponseType.COMMAND_SUCCESS, "Из коллекции были удалены элементы, меньшие заданного!\n");
        } catch (InvalidInputException e) {
            return new Response(ResponseType.COMMAND_ERROR, e.getMessage());
        }

    }
    public String getCommandName() {
        return "remove_greater";
    }
}
