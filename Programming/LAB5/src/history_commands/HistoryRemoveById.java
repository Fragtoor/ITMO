package history_commands;

import models.MusicBand;
/**
 * Хранит в себе имя команды {@code remove_by_id}, {@code id} удаленного объекта, удаленный объект {@link models.MusicBand}.
 */
public class HistoryRemoveById extends HistoryCommand {
    /**
     * {@code id} удаленного объекта
     */
    private final int id;
    /**
     * Удалённый объект {@link models.MusicBand}
     */
    private final MusicBand band;
    /**
     * Создание истории команды {@code remove_by_id}.
     *
     * @param commandName название команды
     */
    public HistoryRemoveById(String commandName, int id, MusicBand band) {
        super(commandName);
        this.band = band;
        this.id = id;
    };
    /**
     * Получить {@code id}
     *
     * @return Возвращает {@code id}
     */
    public int getId() {
        return id;
    }
    /**
     * Получить {@code band}
     *
     * @return Возвращает {@code band}
     */
    public MusicBand getBand() {
        return band;
    }
}
