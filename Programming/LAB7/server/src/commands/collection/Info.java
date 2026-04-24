package commands.collection;

import commands.Command;
import managers.CollectionManager;

public class Info extends Command {
    private final CollectionManager cm;
    public Info(CollectionManager cm) {
        this.cm = cm;
    }

    public String execute(Object... params) {
        String result = cm.info();
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "info";
    }
}
