package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import managers.CollectionManager;

import java.util.LinkedHashSet;


public class Clear extends Command {
    private final CollectionManager cm;
    public Clear(CollectionManager cm) {
        this.cm = cm;
    }

    public void undo() {
        cm.setCollection(new LinkedHashSet<>(cm.getBackupCollection()));
    }
    public Response execute(Object... params) {
        cm.setBackupCollection(new LinkedHashSet<>(cm.getCollection()));
        String message = cm.clear();
        cm.addToCommandsList(this);
        return new Response(ResponseType.COMMAND_SUCCESS, message);
    }
    public String getCommandName() {
        return "clear";
    }
}
