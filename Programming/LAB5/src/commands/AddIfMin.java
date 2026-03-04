package commands;

import main_classes.ApplicationContext;
import history_commands.HistoryAddIfMin;
import tools.CollectionManager;

/**
 * Реализует команду {@code add_if_min}, которая добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
 * Значения полей для создания объекта {@link models.MusicBand} вводятся один за другим построчно.
 */
public class AddIfMin extends Command {
    /**
     * Создание команды {@code add_if_min}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public AddIfMin(Object parameter) {
        super(parameter);
    }
    /**
     * Откат команды {@code add_if_min}.
     */
    public void undo() {
        if (((HistoryAddIfMin) ApplicationContext.commandsList.peek()).isResult()) CollectionManager.removeById(CollectionManager.getMaxId());
    }
    /**
     * Выполнение команды {@code add_if_min}.
     */
    public void execute() {
        boolean result = CollectionManager.addIfMin();
        ApplicationContext.commandsList.add(new HistoryAddIfMin("add_if_min", result));
    }
}
