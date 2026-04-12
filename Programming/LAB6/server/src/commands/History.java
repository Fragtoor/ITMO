package commands;

import tools.CollectionManager;


public class History extends Command {
    public History(CollectionManager cm) {super(cm);}

    public String execute(Object... params) {
        String result = cm.history();
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "history";
    }
}
