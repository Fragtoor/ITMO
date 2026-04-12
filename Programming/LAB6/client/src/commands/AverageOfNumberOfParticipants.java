package commands;

import general.Request;
import models.MusicBand;
import tools.MusicBandCreate;

/**
 * Реализует команду {@code average_of_number_of_participants}, которая выводит среднее значение поля {@code numberOfParticipants} для всех элементов коллекции.
 */
public class AverageOfNumberOfParticipants extends CommandClient {
    /**
     * Создание команды {@code average_of_number_of_participants}.
     *
     * @param parameter параметр, который передаётся команде в командной строке (ни на что не влияет)
     */
    public AverageOfNumberOfParticipants(Object parameter) {
        super(parameter);
    }
    /**
     * Выполнение команды {@code average_of_number_of_participants}.
     */
    public Request<?, ?> toRequest() {
        return new Request<>("average_of_number_of_participants", null, null, getFromTheFile());
    }
}
