package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import managers.CollectionManager;


public class AverageOfNumberOfParticipants extends Command {
    private final CollectionManager cm;
    public AverageOfNumberOfParticipants(CollectionManager cm) {
        this.cm = cm;
    }
    public Response execute(Object... params) {
        String message = cm.averageOfNumberOfParticipants();
        cm.addToCommandsList(this);
        return new Response(ResponseType.COMMAND_SUCCESS, message);
    }
    public String getCommandName() {
        return "average_of_number_of_participants";
    }
}
