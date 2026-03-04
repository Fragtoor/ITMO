package commands;

import exceptions.InvalidInputException;
import tools.CollectionManager;

/**
 * Реализует команду {@code execute_script file_name}, которая считывает и исполняет скрипт из указанного csv файла {@code file_name}.
 * В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
 */
public class ExecuteScript extends Command{
    /**
     * Создает команду {@code execute_script}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public ExecuteScript(Object parameter) {
        super(parameter);
    }
    /**
     * Проверка значения параметра, переданного команде {@code execute_script}.
     *
     * <p>Аргумент {@code file_name} не должен быть {@code null}</p>
     */
    public void validate() {
        if (getParameter() == null) {
            throw new InvalidInputException("У execute_script должен быть аргумент file_name!\n");
        }
    }
    /**
     * Выполнение команды {@code execute_script}.
     */
    public void execute() {
        CollectionManager.executeScript((String)getParameter());
    }
}
