package commands.collection;

import commands.CommandClient;
import common.net.request.CollectionRequest;
import common.net.request.Request;

/**
 * Реализует команду {@code clear}, которая очищает коллекцию.
 */
public class Clear extends CommandClient {
    /**
     * Создает команду {@code clear}.
     *
     * @param params параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Clear(String... params) {
        super(params);
    }

    public Request toRequest() {
        return new CollectionRequest(getUser(), "clear", getFromTheFile());
    }
}
