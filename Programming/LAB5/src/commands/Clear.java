package commands;

import exceptions.InvalidInputException;
import main_classes.Main;
import tools.CollectionManager;
/**
 * Реализует команду clear, которая очищает коллекцию
 *
 * @author alexSIV
 * @version 1.0
 */
public class Clear extends Command {
    /**
     * Создает команду {@code clear}.
     */
    public Clear(Object parameter) {
        super(parameter);
    }


    public void execute() {
        Main.commandsList.add("clear");
        CollectionManager.clear();
    }
}
