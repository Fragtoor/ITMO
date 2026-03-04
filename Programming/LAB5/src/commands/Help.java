package commands;

import history_commands.HistoryHelp;
import main_classes.ApplicationContext;
import tools.CollectionManager;

/**
 * Реализует команду {@code help}, которая выводит справку по доступным командам.
 */
public class Help extends Command {
    /**
     * Создает команду {@code help}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Help (Object parameter) {
        super(parameter);
    }
    /**
     * Выполнение команды {@code help}.
     */
    public void execute() {
        ApplicationContext.commandsList.add(new HistoryHelp("help"));
        CollectionManager.help();
    }
}
