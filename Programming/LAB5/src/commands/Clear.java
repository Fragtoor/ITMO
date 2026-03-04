package commands;

import history_commands.HistoryClear;
import main_classes.ApplicationContext;
import tools.CollectionManager;

import java.util.LinkedHashSet;

/**
 * Реализует команду {@code clear}, которая очищает коллекцию.
 */
public class Clear extends Command {
    /**
     * Создает команду {@code clear}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Clear(Object parameter) {
        super(parameter);
    }
    /**
     * Откат команды {@code clear}.
     */
    public void undo() {
        ApplicationContext.collection = new LinkedHashSet<>(ApplicationContext.backupCollection);
    }
    /**
     * Выполнение команды {@code clear}.
     */
    public void execute() {
        ApplicationContext.backupCollection = new LinkedHashSet<>(ApplicationContext.collection);
        CollectionManager.clear();
        ApplicationContext.commandsList.add(new HistoryClear("clear"));
    }
}
