package commands.collection;

import commands.CommandClient;
import common.net.request.CollectionRequest;
import common.net.request.Request;
import common.models.MusicBand;
import tools.MusicBandCreate;

/**
 * Реализует команду {@code add_if_min}, которая добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
 * Значения полей для создания объекта {@link MusicBand} вводятся один за другим построчно.
 */
public class AddIfMin extends CommandClient {
    private MusicBand band;
    /**
     * Создание команды {@code add_if_min}.
     *
     * @param params параметр, который передаётся команде в командной строке
     */
    public AddIfMin(MusicBand band, String... params) {
        super(params);
        this.band = band;
    }
    public void prepareData() {
        if (this.band == null) {
            this.band = MusicBandCreate.create();
        }
    }

    public AddIfMin(String... params) {
        super(params);
    }
    /**
     * Выполнение команды {@code add_if_min}.
     */
    public Request toRequest() {
        return new CollectionRequest(getUser(), "add_if_min", band);
    }
}
