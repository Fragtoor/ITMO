package commands.collection;

import commands.CommandClient;
import common.general.CollectionRequest;
import common.general.Request;

/**
 * Реализует команду {@code history}, которая выводит последние 10 команд (без их аргументов).
 */
public class History extends CommandClient {
    /**
     * Создает команду {@code history}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public History(Object parameter) {
        super(parameter);
    }
    public Request toRequest() {
        return new CollectionRequest<>(getUser(), "history", null, null, getFromTheFile());
    }
}
