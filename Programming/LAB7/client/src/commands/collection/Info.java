package commands.collection;

import commands.CommandClient;
import common.net.request.CollectionRequest;
import common.net.request.Request;

/**
 * Реализует команду {@code info}, которая выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов).
 */
public class Info extends CommandClient {
    /**
     * Создает команду {@code info}.
     *
     * @param params параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Info(String... params) {
        super(params);
    }
    public Request toRequest() {
        return new CollectionRequest(getUser(), "info", getFromTheFile());
    }
}
