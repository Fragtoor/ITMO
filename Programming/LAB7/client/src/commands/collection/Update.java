package commands.collection;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.net.request.CollectionRequest;
import common.net.request.Request;
import common.models.MusicBand;
import tools.MusicBandCreate;
import common.tools.Validator;

/**
 * Реализует команду {@code update id}, которая обновляет значение элемента коллекции, {@code id} которого равен заданному.
 */
public class Update extends CommandClient {
    /**
     * Создает команду {@code update}.
     *
     * @param params параметр, который передаётся команде в командной строке
     */
    public Update(String... params) {
        super(params);
    }
    /**
     * Проверка значения параметра {@code id}, переданного команде {@code update}.
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
            throw new InvalidInputException("У update должен быть аргумент id - целое положительное число типа int!\n");
        }
    }

    /**
     * Выполнение команды {@code back}.
     */
    public Request toRequest() {
        MusicBand band = MusicBandCreate.create();
        return new CollectionRequest(getUser(), "update", getFromTheFile(), getParams()[0], band);
    }
}
