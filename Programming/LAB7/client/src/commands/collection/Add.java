package commands.collection;

import commands.CommandClient;
import common.general.Request;

import common.models.MusicBand;
import tools.MusicBandCreate;

/**
 * Реализует команду {@code add}, которая добавляет новый элемент типа {@link MusicBand} в коллекцию {@code collection}.
 * Значения полей для создания объекта {@link MusicBand} вводятся один за другим построчно.
 */
public class Add extends CommandClient {
    /**
     * Создание команды {@code add}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public Add(Object parameter) {
        super(parameter);
    }
    /**
     * Начало выполнения команды {@code add}.
     */
    public Request<?, ?> toRequest() {
        MusicBand band = MusicBandCreate.create();
        return new Request<>("add", null, band, getFromTheFile());
    }
}
