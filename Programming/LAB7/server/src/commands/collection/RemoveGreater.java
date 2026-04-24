package commands.collection;

import commands.Command;
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

    public String execute(Object... params) {
        MusicBand band = (MusicBand) params[0];

        listDelete = cm.removeGreater(band);
        cm.addToCommandsList(this);
        if (listDelete.isEmpty()) return "В коллекции не нашлись объекты, меньшие заданного\n";
        return "Из коллекции были удалены элементы, меньшие заданного!\n";
    }
    public String getCommandName() {
        return "remove_greater";
    }
}
