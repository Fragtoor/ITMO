package commands.other;

import commands.CommandClient;
import common.net.request.Request;

/**
 * Реализует команду {@code exit}, которая завершает программу (без сохранения в файл).
 */
public class Exit extends CommandClient {
    /**
     * Создает команду {@code exit}.
     *
     * @param params параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Exit(String... params) {
        super(params);
    }

    public Request toRequest() {
        return new Request(getUser(), "exit");
    }
}
