package commands.collection;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.general.CollectionRequest;
import common.general.Request;
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
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public Update(Object parameter) {
        super(parameter);
    }
    /**
     * Проверка значения параметра {@code id}, переданного команде {@code update}.
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
            throw new InvalidInputException("У update должен быть аргумент id - целое положительное число типа int!\n");
        }
    }

    /**
     * Выполнение команды {@code back}.
     */
    public Request toRequest() {
        MusicBand band = MusicBandCreate.create();
        return new CollectionRequest<>(getUser(), "update", getParameter(), band, getFromTheFile());
    }
}
