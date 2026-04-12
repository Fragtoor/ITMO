package commands;

import tools.CollectionManager;

public class SumOfNumberOfParticipants extends Command {
    public SumOfNumberOfParticipants(CollectionManager cm) {super(cm);}
    public String execute(Object... params) {
        String result = cm.sumOfNumberOfParticipants();
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "sum_of_number_of_participants";
    }
}
