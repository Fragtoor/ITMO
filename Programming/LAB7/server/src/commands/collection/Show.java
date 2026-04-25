package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import managers.CollectionManager;


public class Show extends Command {
    private final CollectionManager cm;
    public Show(CollectionManager cm) {
        this.cm = cm;
    }

    public Response execute(Object... params) {
        cm.addToCommandsList(this);
        String[] result = cm.show();
        return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);

    }
    public String getCommandName() {
        return "show";
    }
}
