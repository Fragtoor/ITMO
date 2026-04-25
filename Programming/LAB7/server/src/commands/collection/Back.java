package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import managers.CollectionManager;


public class Back extends Command {
    private final CollectionManager cm;
    public Back(CollectionManager cm) {
        this.cm = cm;
    }

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

    public Response execute(Object... params) {
        String message = cm.back(Integer.parseInt((String)params[0]));
        return new Response(ResponseType.COMMAND_SUCCESS, message);
    }
    public String getCommandName() {
        return "back";
    }
}
