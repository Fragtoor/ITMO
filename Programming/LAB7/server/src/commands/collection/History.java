package commands.collection;

import commands.Command;
import managers.CollectionManager;


public class History extends Command {
    private final CollectionManager cm;
    public History(CollectionManager cm) {
        this.cm = cm;
    }

    public String execute(Object... params) {
        String result = cm.history();
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "history";
    }
}
