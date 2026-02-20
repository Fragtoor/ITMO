package commands;

import exceptions.InvalidInputException;
import main_classes.Main;
import tools.CollectionManager;

/**
 * Реализует команду execute_script, которая считывает и исполняет скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
 *
 * @author alexSIV
 * @version 1.0
 */
public class ExecuteScript extends Command{
    /**
     * Создает команду {@code execute_script}.
     */
    public ExecuteScript(Object parameter) {
        super(parameter);
    }

    public void validate() {
        if (this.parameter == null) {
            throw new InvalidInputException("У execute_script должен быть аргумент file_name!\n");
        }
    }

    public void execute() {
        Main.commandsList.add("execute_script");
        CollectionManager.executeScript((String)this.parameter);
    }
}
