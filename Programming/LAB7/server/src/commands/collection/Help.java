package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import managers.CollectionManager;


public class Help extends Command {
    private final CollectionManager cm;
    public Help(CollectionManager cm) {
        this.cm = cm;
    }

    public Response execute(Object... params) {
        String[] result = cm.help();
        cm.addToCommandsList(this);
        return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
    }
    public String getCommandName() {
        return "help";
    }
}
