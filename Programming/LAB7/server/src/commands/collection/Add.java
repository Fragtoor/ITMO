package commands.collection;

import commands.Command;
import common.models.MusicBand;
import managers.CollectionManager;

public class Add extends Command {
    private final CollectionManager cm;
    public Add(CollectionManager cm) {
        this.cm = cm;
    }

    public void undo() {
        cm.removeById(cm.getMaxId());
    }

    public boolean validateParams(Object... params) {
        return (params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate());
    }

    public String execute(Object... params) {
        MusicBand band = (MusicBand) params[0];
        String result = cm.add(band);
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "add";
    }
}
