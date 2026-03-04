package commands;

import history_commands.HistoryExit;
import main_classes.ApplicationContext;
import tools.CollectionManager;
/**
 * Реализует команду {@code exit}, которая завершает программу (без сохранения в файл).
 */
public class Exit extends Command {
    /**
     * Создает команду {@code exit}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Exit(Object parameter) {
        super(parameter);
    }
    /**
     * Выполнение команды {@code exit}.
     */
    public void execute() {
        ApplicationContext.commandsList.add(new HistoryExit("exit"));
        CollectionManager.exit();
    }
}
