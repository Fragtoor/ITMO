package commands.collection;

import commands.Command;
import managers.CollectionManager;


public class Help extends Command {
    private final CollectionManager cm;
    public Help(CollectionManager cm) {
        this.cm = cm;
    }

    public String execute(Object... params) {
        String result = cm.help();
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "help";
    }
}
