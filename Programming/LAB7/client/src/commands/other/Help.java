package commands.other;

import commands.CommandClient;
import common.general.CollectionRequest;
import common.general.Request;

/**
 * Реализует команду {@code help}, которая выводит справку по доступным командам.
 */
public class Help extends CommandClient {
    /**
     * Создает команду {@code help}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Help (Object parameter) {
        super(parameter);
    }
    public Request toRequest() {
        return new CollectionRequest<>(getUser(), "help", null, null, getFromTheFile());
    }
}
