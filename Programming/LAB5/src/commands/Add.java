package commands;

import main_classes.ApplicationContext;
import history_commands.HistoryAdd;
import tools.CollectionManager;

/**
 * Реализует команду {@code add}, которая добавляет новый элемент типа {@link models.MusicBand} в коллекцию {@code collection}.
 * Значения полей для создания объекта {@link models.MusicBand} вводятся один за другим построчно.
 */
public class Add extends Command {
    /**
     * Создание команды {@code add}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public Add(Object parameter) {
        super(parameter);
    }
    /**
     * Откат команды {@code add}.
     */
    public void undo() {
        CollectionManager.removeById(CollectionManager.getMaxId());
    }
    /**
     * Начало выполнения команды {@code add}.
     */
    public void execute() {
        CollectionManager.add();
        ApplicationContext.commandsList.add(new HistoryAdd("add"));
    }
}
