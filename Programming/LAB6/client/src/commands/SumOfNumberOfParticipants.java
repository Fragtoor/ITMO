package commands;

import general.Request;

/**
 * Реализует команду {@code sum_of_number_of_participants},
 * которая выводит сумму значений поля {@code numberOfParticipants} для всех элементов коллекции.
 */
public class SumOfNumberOfParticipants extends CommandClient {
    /**
     * Создает команду {@code sum_of_number_of_participants}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public SumOfNumberOfParticipants(Object parameter) {
        super(parameter);
    }
    /**
     * Выполнение команды {@code back}.
     */
    public Request<?, ?> toRequest() {
        return new Request<>("sum_of_number_of_participants", null, null, getFromTheFile());
    }
}
