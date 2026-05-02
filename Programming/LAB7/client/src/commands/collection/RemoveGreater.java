package commands.collection;

import commands.CommandClient;
import common.general.CollectionRequest;
import common.general.Request;
import common.models.MusicBand;
import tools.MusicBandCreate;


/**
 * Реализует команду {@code remove_greater}, которая удаляет из коллекции все элементы, превышающие заданный.
 * Значения полей для создания объекта {@link MusicBand} вводятся один за другим построчно.
 *
 */
public class RemoveGreater extends CommandClient {
    /**
     * Создает команду {@code remove_greater}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public RemoveGreater(Object parameter) {
        super(parameter);
    }
    /**
     * Выполнение команды {@code back}.
     */
    public Request toRequest() {
        MusicBand band = MusicBandCreate.create();
        return new CollectionRequest<>(getUser(), "remove_greater", null, band, getFromTheFile());
    }
}
