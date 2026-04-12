package commands;

import tools.CollectionManager;


public class AverageOfNumberOfParticipants extends Command {
    public AverageOfNumberOfParticipants(CollectionManager cm) {super(cm);}

    public String execute(Object... params) {
        String result = cm.averageOfNumberOfParticipants();
        cm.addToCommandsList(this);
        return result;
    }
    public String getCommandName() {
        return "average_of_number_of_participants";
    }
}
