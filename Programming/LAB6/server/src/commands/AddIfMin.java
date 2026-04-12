package commands;

import models.MusicBand;
import tools.CollectionManager;

public class AddIfMin extends Command {
    private boolean isAdd;
    public AddIfMin(CollectionManager cm) {super(cm);}

    public void undo() {
        if (isAdd) cm.removeById(cm.getMaxId());
    }

    public boolean validateParams(Object... params) {
        return (params.length != 0) && (params[0] instanceof MusicBand band) && (band.validate());
    }

    public String execute(Object... params) {
        MusicBand band = (MusicBand) params[0];
        boolean result = cm.addIfMin(band);
        isAdd = result;
        cm.addToCommandsList(this);

        if (result) {return "Элемент добавлен в коллекцию!\n";}
        return "Элемент не добавлен в коллекцию!\n";
    }
    public String getCommandName() {return "add_if_min";}
}
