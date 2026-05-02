package commands.collection;

import commands.CommandClient;
import common.general.CollectionRequest;
import common.general.Request;

/**
 * Реализует команду {@code show}, которая выводит в стандартный поток вывода все элементы коллекции в строковом представлении.
 */
public class Show extends CommandClient {
    /**
     * Создает команду {@code show}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Show(Object parameter) {
        super(parameter);
    }
    /**
     * Выполнение команды {@code back}.
     */
    public Request toRequest() {
        return new CollectionRequest<>(getUser(), "show", null, null, getFromTheFile());
    }
}
