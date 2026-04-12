package commands;

import tools.CollectionManager;


public class Back extends Command{
    public Back(CollectionManager cm) {super(cm);}

    public boolean validateParams(Object... params) {
        if (params.length == 0 || !(params[0] instanceof String n)) return false;
        int number;
        try {
            number = Integer.parseInt(n);
            if (number < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public String execute(Object... params) {
        return cm.back(Integer.parseInt((String)params[0]));
    }
    public String getCommandName() {
        return "back";
    }
}
