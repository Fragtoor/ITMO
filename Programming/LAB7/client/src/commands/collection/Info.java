package commands.collection;

import commands.CommandClient;
import common.general.CollectionRequest;
import common.general.Request;

/**
 * Реализует команду {@code info}, которая выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов).
 */
public class Info extends CommandClient {
    /**
     * Создает команду {@code info}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Info(Object parameter) {
        super(parameter);
    }
    public Request toRequest() {
        return new CollectionRequest<>(getUser(), "info", null, null, getFromTheFile());
    }
}
