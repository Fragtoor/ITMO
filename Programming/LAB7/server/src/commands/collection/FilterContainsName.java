package commands.collection;

import commands.Command;
import managers.CollectionManager;


public class FilterContainsName extends Command {
    private final CollectionManager cm;
    public FilterContainsName(CollectionManager cm) {
        this.cm = cm;
    }

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
