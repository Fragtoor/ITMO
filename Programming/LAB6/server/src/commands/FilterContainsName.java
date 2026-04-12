package commands;

import tools.CollectionManager;


public class FilterContainsName extends Command{
    public FilterContainsName(CollectionManager cm) {super(cm);}

    public boolean validateParams(Object... params) {
        return params.length != 0 && (params[0] instanceof String);
    }

    public String execute(Object... params) {
        String result = cm.filterContainsName((String)params[0]);
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "filter_contains_name";
    }
}
