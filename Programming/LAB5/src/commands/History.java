package commands;

import history_commands.HistoryHistory;
import main_classes.ApplicationContext;
import tools.CollectionManager;
/**
 * Реализует команду {@code history}, которая выводит последние 10 команд (без их аргументов).
 */
public class History extends Command {
    /**
     * Создает команду {@code history}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public History(Object parameter) {
        super(parameter);
    }
    /**
     * Выполнение команды {@code history}.
     */
    public void execute() {
        ApplicationContext.commandsList.add(new HistoryHistory("history"));
        CollectionManager.history();
    }
}
