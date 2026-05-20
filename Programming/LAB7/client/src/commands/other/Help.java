package commands.other;

import commands.CommandClient;
import common.net.request.CollectionRequest;
import common.net.request.Request;

/**
 * Реализует команду {@code help}, которая выводит справку по доступным командам.
 */
public class Help extends CommandClient {
    /**
     * Создает команду {@code help}.
     *
     * @param params параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Help(String... params) {
        super(params);
    }
    public Request toRequest() {
        return new CollectionRequest(getUser(), "help");
    }
}
