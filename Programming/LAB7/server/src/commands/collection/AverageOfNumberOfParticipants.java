package commands.collection;

import commands.Command;
import managers.CollectionManager;


public class AverageOfNumberOfParticipants extends Command {
    private final CollectionManager cm;
    public AverageOfNumberOfParticipants(CollectionManager cm) {
        this.cm = cm;
    }
    public String execute(Object... params) {
        String result = cm.averageOfNumberOfParticipants();
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "average_of_number_of_participants";
    }
}
