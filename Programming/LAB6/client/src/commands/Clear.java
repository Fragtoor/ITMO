package commands;

import general.Request;

/**
 * Реализует команду {@code clear}, которая очищает коллекцию.
 */
public class Clear extends CommandClient {
    /**
     * Создает команду {@code clear}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Clear(Object parameter) {
        super(parameter);
    }

    public Request<?, ?> toRequest() {
        return new Request<>("clear", null, null, getFromTheFile());
    }
}
