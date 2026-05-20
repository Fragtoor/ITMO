package commands.collection;

import commands.CommandClient;
import common.net.request.CollectionRequest;
import common.net.request.Request;

/**
 * Реализует команду {@code history}, которая выводит последние 10 команд (без их аргументов).
 */
public class History extends CommandClient {
    /**
     * Создает команду {@code history}.
     *
     * @param params параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public History(String... params) {
        super(params);
    }
    public Request toRequest() {
        return new CollectionRequest(getUser(), "history");
    }
}
