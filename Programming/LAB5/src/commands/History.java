package commands;

import exceptions.InvalidInputException;
import main_classes.Main;
import tools.CollectionManager;
/**
 * Реализует команду history, которая выводит последние 10 команд (без их аргументов)
 *
 * @author alexSIV
 * @version 1.0
 */
public class History extends Command {
    /**
     * Создает команду {@code help}.
     */
    public History(Object parameter) {
        super(parameter);
    }

    public void execute() {
        Main.commandsList.add("history");
        CollectionManager.history();
    }
}
