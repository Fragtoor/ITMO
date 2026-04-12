package commands;

import tools.CollectionManager;

import java.util.LinkedHashSet;


public class Clear extends Command {
    public Clear(CollectionManager cm) {super(cm);}

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
