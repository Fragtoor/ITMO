package commands.collection;

import commands.Command;
import managers.CollectionManager;

public class SumOfNumberOfParticipants extends Command {
    private final CollectionManager cm;
    public SumOfNumberOfParticipants(CollectionManager cm) {
        this.cm = cm;
    }
    public String execute(Object... params) {
        String result = cm.sumOfNumberOfParticipants();
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "sum_of_number_of_participants";
    }
}
