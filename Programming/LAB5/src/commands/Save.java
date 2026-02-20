package commands;

import exceptions.InvalidInputException;
import tools.CollectionManager;
/**
 * Реализует команду save, которая сохраняет коллекцию в файл
 *
 * @author alexSIV
 * @version 1.0
 */
public class Save extends Command {
    /**
     * Создает команду {@code save}.
     */
    public Save(Object parameter) {
        super(parameter);
    }

    public void execute() {
        main_classes.Main.commandsList.add("save");
        CollectionManager.save();
    }
}
