package commands.other;

import commands.CommandClient;
import common.general.Request;

/**
 * Реализует команду {@code exit}, которая завершает программу (без сохранения в файл).
 */
public class Exit extends CommandClient {
    /**
     * Создает команду {@code exit}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Exit(Object parameter) {
        super(parameter);
    }

    public Request<?, ?> toRequest() {
        return new Request<>("exit", null, null, getFromTheFile());
    }
}
