package commands.collection;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.general.Request;

/**
 * Реализует команду {@code filter_contains_name name}, которая выводит элементы, значение поля {@code name} которых содержит заданную подстроку.
 */
public class FilterContainsName extends CommandClient {
    /**
     * Создает команду {@code filter_contains_name}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public FilterContainsName(Object parameter) {
        super(parameter);
    }
    /**
     * Проверка значения параметра {@code name}, переданного команде {@code filter_contains_name}.
     *
     * <p>Аргумент {@code name} не должен быть {@code null}</p>
     */
    public void validate() {
        if (getParameter() == null) {
            throw new InvalidInputException("У filter_contains_name должен быть аргумент name!\n");
        }
    }
    public Request<?, ?> toRequest() {
        return new Request<>("filter_contains_name", getParameter(), null, getFromTheFile());
    }
}
