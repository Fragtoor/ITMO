package commands.collection;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.net.request.CollectionRequest;
import common.net.request.Request;
import common.tools.Validator;

/**
 * Реализует команду {@code remove_by_id id}, которая удаляет элемент из коллекции по его {@code id}.
 */
public class RemoveById extends CommandClient {
    /**
     * Создает команду {@code remove_by_id}.
     *
     * @param params параметр, который передаётся команде в командной строке
     */
    public RemoveById(String... params) {
        super(params);
    }
    /**
     * Проверка значения параметра {@code id}, переданного команде {@code remove_by_id}.
     *
     * <p>Аргумент {@code id} должен быть положительным целым числом типа {@code int}</p>
     */
    public void validate() {
        try {
            if (getParams() == null || getParams().length == 0) {
                throw new InvalidInputException("");
            } else if (!Validator.isInt(getParams()[0])) {
                throw new InvalidInputException("");
            } else if (Integer.parseInt(getParams()[0]) <= 0) {
                throw new InvalidInputException("");
            }
        } catch(InvalidInputException e){
            throw new InvalidInputException("У remove_by_id должен быть аргумент id - целое положительное число!\n");
        }
    }
    /**
     * Выполнение команды {@code back}.
     */
    public Request toRequest() {
        return new CollectionRequest(getUser(), "remove_by_id", getParams()[0]);
    }
}
