package commands;

import tools.CollectionManager;

public class Info extends Command {
    public Info(CollectionManager cm) {super(cm);}

    public String execute(Object... params) {
        String result = cm.info();
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "info";
    }
}
