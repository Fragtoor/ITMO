package commands.collection;

import commands.CommandClient;
import common.general.CollectionRequest;
import common.general.Request;
import common.models.MusicBand;
import tools.MusicBandCreate;

/**
 * Реализует команду {@code add_if_min}, которая добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
 * Значения полей для создания объекта {@link MusicBand} вводятся один за другим построчно.
 */
public class AddIfMin extends CommandClient {
    /**
     * Создание команды {@code add_if_min}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public AddIfMin(Object parameter) {
        super(parameter);
    }
    /**
     * Выполнение команды {@code add_if_min}.
     */
    public Request toRequest() {
        MusicBand band = MusicBandCreate.create();
        return new CollectionRequest<>(getUser(), "add_if_min", null, band, getFromTheFile());
    }
}
