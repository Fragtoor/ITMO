package commands.collection;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.net.request.CollectionRequest;
import common.net.request.Request;
import common.tools.Validator;

/**
 * Реализует команду {@code back n}, которая отменяет действия последних {@code n} команд.
 */
public class Back extends CommandClient {
    /**
     * Создание команды {@code back}.
     *
     * @param params параметр, который передаётся команде в командной строке
     */
    public Back(String... params) {
        super(params);
    }
    /**
     * Проверка значения параметра {@code n}, переданного команде {@code back}.
     *
     * <p>Аргумент {@code n} должен быть положительным целым числом типа {@code int}</p>
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
        } catch (InvalidInputException e){
            throw new InvalidInputException("У back должен быть аргумент n - целое положительное число!\n");
        }
    }
    /**
     * Выполнение команды {@code back}.
     */
    public Request toRequest() {
        return new CollectionRequest(getUser(), "back", getParams()[0]);
    }
}
