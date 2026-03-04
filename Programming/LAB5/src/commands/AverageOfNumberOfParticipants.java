package commands;

import history_commands.HistoryAverageOfNUmberOfParticipants;
import main_classes.ApplicationContext;
import tools.CollectionManager;
/**
 * Реализует команду {@code average_of_number_of_participants}, которая выводит среднее значение поля {@code numberOfParticipants} для всех элементов коллекции.
 */
public class AverageOfNumberOfParticipants extends Command {
    /**
     * Создание команды {@code average_of_number_of_participants}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public AverageOfNumberOfParticipants(Object parameter) {
        super(parameter);
    }
    /**
     * Выполнение команды {@code average_of_number_of_participants}.
     */
    public void execute() {
        ApplicationContext.commandsList.add(new HistoryAverageOfNUmberOfParticipants("average_of_number_of_participants"));
        CollectionManager.averageOfNumberOfParticipants();
    }
}
