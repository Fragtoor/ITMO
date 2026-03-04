package commands;

import history_commands.HistoryRemoveGreater;
import main_classes.ApplicationContext;
import models.MusicBand;
import tools.CollectionManager;

import java.util.LinkedHashSet;

/**
 * Реализует команду {@code remove_greater}, которая удаляет из коллекции все элементы, превышающие заданный.
 * Значения полей для создания объекта {@link models.MusicBand} вводятся один за другим построчно.
 *
 */
public class RemoveGreater extends Command {
    /**
     * Создает команду {@code remove_greater}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public RemoveGreater(Object parameter) {
        super(parameter);
    }
    /**
     * Откат команды {@code remove_greater}.
     */
    public void undo() {
        LinkedHashSet< MusicBand > list = ((HistoryRemoveGreater)ApplicationContext.commandsList.peek()).getList();
        ApplicationContext.collection.addAll(list);
    }
    /**
     * Выполнение команды {@code remove_greater}.
     */
    public void execute() {
        var list = CollectionManager.removeGreater();
        ApplicationContext.commandsList.add(new HistoryRemoveGreater("remove_greater", list));
    }
}
