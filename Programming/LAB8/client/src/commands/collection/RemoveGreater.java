package commands.collection;

import commands.CommandClient;
import common.net.request.CollectionRequest;
import common.net.request.Request;
import common.models.MusicBand;
import tools.MusicBandCreate;


/**
 * Реализует команду {@code remove_greater}, которая удаляет из коллекции все элементы, превышающие заданный.
 * Значения полей для создания объекта {@link MusicBand} вводятся один за другим построчно.
 *
 */
public class RemoveGreater extends CommandClient {
    private MusicBand band;
    /**
     * Создает команду {@code remove_greater}.
     *
     * @param params параметр, который передаётся команде в командной строке
     */
    public RemoveGreater(MusicBand band, String... params) {
        super(params);
        this.band = band;
    }

    public void prepareData() {
        if (this.band == null) {
            this.band = MusicBandCreate.create();
        }
    }

    public RemoveGreater(String... params) {
        super(params);
    }
    /**
     * Выполнение команды {@code back}.
     */
    public Request toRequest() {
        band = MusicBandCreate.create();
        return new CollectionRequest(getUser(), "remove_greater", band);
    }
}
