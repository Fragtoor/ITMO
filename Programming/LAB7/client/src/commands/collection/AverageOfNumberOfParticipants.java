package commands.collection;

import commands.CommandClient;
import common.net.request.CollectionRequest;
import common.net.request.Request;

/**
 * Реализует команду {@code average_of_number_of_participants}, которая выводит среднее значение поля {@code numberOfParticipants} для всех элементов коллекции.
 */
public class AverageOfNumberOfParticipants extends CommandClient {
    /**
     * Создание команды {@code average_of_number_of_participants}.
     *
     * @param params параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public AverageOfNumberOfParticipants(String... params) {
        super(params);
    }
    /**
     * Выполнение команды {@code average_of_number_of_participants}.
     */
    public Request toRequest() {
        return new CollectionRequest(getUser(), "average_of_number_of_participants");
    }
}
