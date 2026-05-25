package commands.collection;

import commands.CommandClient;
import common.net.request.CollectionRequest;
import common.net.request.Request;

import common.models.MusicBand;
import tools.MusicBandCreate;

/**
 * Реализует команду {@code add}, которая добавляет новый элемент типа {@link MusicBand} в коллекцию {@code collection}.
 * Значения полей для создания объекта {@link MusicBand} вводятся один за другим построчно.
 */
public class Add extends CommandClient {
    private MusicBand band;
    /**
     * Создание команды {@code add}.
     *
     * @param params параметр, который передаётся команде в командной строке
     */
    public Add(MusicBand band, String... params) {
        super(params);
        this.band = band;
    }

    public Add(String... params) {
        super(params);
    }

    public void prepareData() {
        if (this.band == null) {
            this.band = MusicBandCreate.create();
        }
    }

    /**
     * Начало выполнения команды {@code add}.
     */
    public Request toRequest() {
        return new CollectionRequest(getUser(), "add",  band);
    }
}
