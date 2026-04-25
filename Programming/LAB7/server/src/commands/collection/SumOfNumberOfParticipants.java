package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import managers.CollectionManager;

public class SumOfNumberOfParticipants extends Command {
    private final CollectionManager cm;
    public SumOfNumberOfParticipants(CollectionManager cm) {
        this.cm = cm;
    }
    public Response execute(Object... params) {
        String message = cm.sumOfNumberOfParticipants();
        cm.addToCommandsList(this);
        return new Response(ResponseType.COMMAND_SUCCESS, message);
    }
    public String getCommandName() {
        return "sum_of_number_of_participants";
    }
}
