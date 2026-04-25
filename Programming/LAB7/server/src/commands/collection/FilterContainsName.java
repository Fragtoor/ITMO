package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import managers.CollectionManager;


public class FilterContainsName extends Command {
    private final CollectionManager cm;
    public FilterContainsName(CollectionManager cm) {
        this.cm = cm;
    }

    public boolean validateParams(Object... params) {
        return params.length != 0 && (params[0] instanceof String);
    }

    public Response execute(Object... params) {
        String[] result = cm.filterContainsName((String)params[0]);
        cm.addToCommandsList(this);
        return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
    }
    public String getCommandName() {
        return "filter_contains_name";
    }
}
