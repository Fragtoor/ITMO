package history_commands;

import models.MusicBand;
/**
 * Хранит в себе имя команды {@code update}, обновлённый объект {@link models.MusicBand} и его {@code id}.
 */
public class HistoryUpdate extends HistoryCommand {
    /**
     * {@code id} обновленного объекта
     */
    private int id;
    /**
     * Обновлённый объект {@link models.MusicBand}
     */
    private MusicBand band;
    /**
     * Создание истории команды {@code update}.
     *
     * @param commandName название команды
     * @param id идентификатор обновленного объекта
     * @param band обновленный объект {@link models.MusicBand}
     */
    public HistoryUpdate(String commandName, int id, MusicBand band) {
        super(commandName);
        this.id = id;
        this.band = band;
    }
    /**
     * Получить {@code band}.
     *
     * @return Возвращает {@code band}
     */
    public MusicBand getBand() {
        return band;
    }
    /**
     * Получить {@code id}.
     *
     * @return Возвращает {@code id}
     */
    public int getId() {
        return id;
    }
    /**
     * Установить {@code id}.
     *
     * @param id новый {@code id}
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Установить {@code band}.
     *
     * @param band новый {@code band}
     */
    public void setBand(MusicBand band) {
        this.band = band;
    }
}
