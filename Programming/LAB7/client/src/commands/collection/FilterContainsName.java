package commands.collection;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.net.request.CollectionRequest;
import common.net.request.Request;

/**
 * Реализует команду {@code filter_contains_name name}, которая выводит элементы, значение поля {@code name} которых содержит заданную подстроку.
 */
public class FilterContainsName extends CommandClient {
    /**
     * Создает команду {@code filter_contains_name}.
     *
     * @param params параметр, который передаётся команде в командной строке
     */
    public FilterContainsName(String... params) {
        super(params);
    }
    /**
     * Проверка значения параметра {@code name}, переданного команде {@code filter_contains_name}.
     *
     * <p>Аргумент {@code name} не должен быть {@code null}</p>
     */
    public void validate() {
        if (getParams() == null || getParams().length == 0) {
            throw new InvalidInputException("У filter_contains_name должен быть аргумент name!\n");
        }
    }
    public Request toRequest() {
        return new CollectionRequest(getUser(), "filter_contains_name", getFromTheFile(), getParams()[0]);
    }
}
