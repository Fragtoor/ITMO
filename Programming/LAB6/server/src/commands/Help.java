package commands;

import tools.CollectionManager;


public class Help extends Command {
    public Help(CollectionManager cm) {super(cm);}

    public String execute(Object... params) {
        String result = cm.help();
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "help";
    }
}
