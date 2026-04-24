package commands.collection;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.general.Request;
import tools.Validator;

/**
 * Реализует команду {@code remove_by_id id}, которая удаляет элемент из коллекции по его {@code id}.
 */
public class RemoveById extends CommandClient {
    /**
     * Создает команду {@code remove_by_id}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public RemoveById(Object parameter) {
        super(parameter);
    }
    /**
     * Проверка значения параметра {@code id}, переданного команде {@code remove_by_id}.
     *
     * <p>Аргумент {@code id} должен быть положительным целым числом типа {@code int}</p>
     */
    public void validate() {
        try {
            if (getParameter() == null) {
                throw new InvalidInputException("");
            } else if (!Validator.isInt(getParameter())) {
                throw new InvalidInputException("");
            } else if (Integer.parseInt((String)getParameter()) <= 0) {
                throw new InvalidInputException("");
            }
        } catch(InvalidInputException e){
            throw new InvalidInputException("У remove_by_id должен быть аргумент id - целое положительное число!\n");
        }
    }
    /**
     * Выполнение команды {@code back}.
     */
    public Request<?, ?> toRequest() {
        return new Request<>("remove_by_id", getParameter(), null, getFromTheFile());
    }
}
