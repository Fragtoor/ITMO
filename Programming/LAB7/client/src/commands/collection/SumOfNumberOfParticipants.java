package commands.collection;

import commands.CommandClient;
import common.net.request.CollectionRequest;
import common.net.request.Request;

/**
 * Реализует команду {@code sum_of_number_of_participants},
 * которая выводит сумму значений поля {@code numberOfParticipants} для всех элементов коллекции.
 */
public class SumOfNumberOfParticipants extends CommandClient {
    /**
     * Создает команду {@code sum_of_number_of_participants}.
     *
     * @param params параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public SumOfNumberOfParticipants(String... params) {
        super(params);
    }
    /**
     * Выполнение команды {@code back}.
     */
    public Request toRequest() {
        return new CollectionRequest(getUser(), "sum_of_number_of_participants", getFromTheFile());
    }
}
