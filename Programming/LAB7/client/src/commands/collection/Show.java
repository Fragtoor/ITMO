package commands.collection;

import commands.CommandClient;
import common.net.request.CollectionRequest;
import common.net.request.Request;

/**
 * Реализует команду {@code show}, которая выводит в стандартный поток вывода все элементы коллекции в строковом представлении.
 */
public class Show extends CommandClient {
    /**
     * Создает команду {@code show}.
     *
     * @param params параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Show(String... params) {
        super(params);
    }
    /**
     * Выполнение команды {@code back}.
     */
    public Request toRequest() {
        return new CollectionRequest(getUser(), "show", getFromTheFile());
    }
}
