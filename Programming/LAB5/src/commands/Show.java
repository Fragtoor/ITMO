package commands;

import history_commands.HistoryShow;
import main_classes.ApplicationContext;
import tools.CollectionManager;

/**
 * Реализует команду {@code show}, которая выводит в стандартный поток вывода все элементы коллекции в строковом представлении.
 */
public class Show extends Command{
    /**
     * Создает команду {@code show}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Show(Object parameter) {
        super(parameter);
    }
    /**
     * Выполнение команды {@code show}.
     */
    public void execute() {
        ApplicationContext.commandsList.add(new HistoryShow("show"));
        CollectionManager.show();
    }
}
