package commands;

import history_commands.HistoryInfo;
import main_classes.ApplicationContext;
import tools.CollectionManager;
/**
 * Реализует команду {@code info}, которая выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов).
 */
public class Info extends Command {
    /**
     * Создает команду {@code info}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Info(Object parameter) {
        super(parameter);
    }
    /**
     * Выполнение команды {@code info}.
     */
    public void execute() {
        ApplicationContext.commandsList.add(new HistoryInfo("info"));
        CollectionManager.info();
    }
}
