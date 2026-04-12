package commands;

import general.Request;

/**
 * Реализует команду {@code info}, которая выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов).
 */
public class Info extends CommandClient {
    /**
     * Создает команду {@code info}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public Info(Object parameter) {
        super(parameter);
    }
    public Request<?, ?> toRequest() {
        return new Request<>("info", null, null, getFromTheFile());
    }
}
