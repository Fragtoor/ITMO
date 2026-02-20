package commands;

import exceptions.InvalidInputException;
import main_classes.Main;
import tools.CollectionManager;

/**
 * Реализует команду show, которая выводит в стандартный поток вывода все элементы коллекции в строковом представлении
 *
 * @author alexSIV
 * @version 1.0
 */
public class Show extends Command{
    /**
     * Создает команду {@code show}.
     */
    public Show(Object parameter) {
        super(parameter);
    }

    public void execute() {
        Main.commandsList.add("show");
        CollectionManager.show();
    }
}
