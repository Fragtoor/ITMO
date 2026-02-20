package commands;

import exceptions.InvalidInputException;
import main_classes.Main;
import tools.CollectionManager;
/**
 * Реализует команду exit, которая завершает программу (без сохранения в файл)
 *
 * @author alexSIV
 * @version 1.0
 */
public class Exit extends Command {
    /**
     * Создает команду {@code exit}.
     */
    public Exit(Object parameter) {
        super(parameter);
    }

    public void execute() {
        Main.commandsList.add("exit");
        CollectionManager.exit();
    }
}
