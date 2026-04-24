package commands.collection;

import commands.Command;
import managers.CollectionManager;

import java.util.LinkedHashSet;


public class Clear extends Command {
    private final CollectionManager cm;
    public Clear(CollectionManager cm) {
        this.cm = cm;
    }

    public void undo() {
        cm.setCollection(new LinkedHashSet<>(cm.getBackupCollection()));
    }
    public String execute(Object... params) {
        cm.setBackupCollection(new LinkedHashSet<>(cm.getCollection()));
        String result = cm.clear();
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "clear";
    }
}
