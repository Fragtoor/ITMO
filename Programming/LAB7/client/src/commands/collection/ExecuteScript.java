package commands.collection;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.general.CollectionRequest;
import common.general.Request;

/**
 * Реализует команду {@code execute_script file_name}, которая считывает и исполняет скрипт из указанного csv файла {@code file_name}.
 * В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
 */
public class ExecuteScript extends CommandClient {
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
    public Request toRequest() {
        return new CollectionRequest<>(getUser(), "execute_script", getParameter(), null, getFromTheFile());
    }
}
