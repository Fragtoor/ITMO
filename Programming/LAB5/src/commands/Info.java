package commands;

import exceptions.InvalidInputException;
import main_classes.Main;
import tools.CollectionManager;
/**
 * Реализует команду info, которая выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов)
 *
 * @author alexSIV
 * @version 1.0
 */
public class Info extends Command {
    /**
     * Создает команду {@code info}.
     */
    public Info(Object parameter) {
        super(parameter);
    }

    public void execute() {
        Main.commandsList.add("info");
        CollectionManager.info();
    }
}
