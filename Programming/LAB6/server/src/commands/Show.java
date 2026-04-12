package commands;

import tools.CollectionManager;


public class Show extends Command{
    public Show(CollectionManager cm) {super(cm);}

    public String execute(Object... params) {
        cm.addToCommandsList(this);
        return cm.show();
    }
    public String getCommandName() {
        return "show";
    }
}
