package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import managers.CollectionManager;

public class Info extends Command {
    private final CollectionManager cm;
    public Info(CollectionManager cm) {
        this.cm = cm;
    }

    public Response execute(Object... params) {
        String[] result = cm.info();
        cm.addToCommandsList(this);
        return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
    }
    public String getCommandName() {
        return "info";
    }
}
