package commands;

import history_commands.HistorySave;
import main_classes.ApplicationContext;
import tools.CollectionManager;
import tools.FileManager;


/**
 * Реализует команду {@code save}, которая сохраняет коллекцию в файл, переданный программе при запуске.
 */
public class Save extends Command {
    /**
     * Создает команду {@code save}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Save(Object parameter) {
        super(parameter);
    }
    /**
     * Откат команды {@code save}.
     */
    public void undo() {
        FileManager.writeToFile(ApplicationContext.FILE_NAME, ((HistorySave)ApplicationContext.commandsList.peek()).getContent());
    }
    /**
     * Выполнение команды {@code save}.
     */
    public void execute() {
        String content = CollectionManager.save();
        if (content != null) ApplicationContext.commandsList.add(new HistorySave("save", content));
    }
}
