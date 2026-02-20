package commands;

import exceptions.InvalidInputException;
import tools.CollectionManager;
/**
 * Реализует команду sum_of_number_of_participants, которая выводит сумму значений поля numberOfParticipants для всех элементов коллекции
 *
 * @author alexSIV
 * @version 1.0
 */
public class SumOfNumberOfParticipants extends Command {
    /**
     * Создает команду {@code sum_of_number_of_participants}.
     */
    public SumOfNumberOfParticipants(Object parameter) {
        super(parameter);
    }

    public void execute() {
        main_classes.Main.commandsList.add("sum_of_number_of_participants");
        CollectionManager.sumOfNumberOfParticipants();
    }
}
