package commands;

import exceptions.InvalidInputException;
import main_classes.Main;
import tools.CollectionManager;
/**
 * Реализует команду average_of_number_of_participants, которая выводит среднее значение поля numberOfParticipants для всех элементов коллекции
 *
 * @author alexSIV
 * @version 1.0
 */
public class AverageOfNumberOfParticipants extends Command {
    /**
     * Создает команду {@code average_of_number_of_participants}.
     */
    public AverageOfNumberOfParticipants(Object parameter) {
        super(parameter);
    }

    public void execute() {
        Main.commandsList.add("average_of_number_of_participants");
        CollectionManager.averageOfNumberOfParticipants();
    }
}
