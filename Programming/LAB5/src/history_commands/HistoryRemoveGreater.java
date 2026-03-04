package history_commands;

import models.MusicBand;
import java.util.LinkedHashSet;
/**
 * Хранит в себе имя команды {@code remove_greater}, список удаленных объектов {@link models.MusicBand}.
 */
public class HistoryRemoveGreater extends HistoryCommand {
    /**
     * Список удаленных объектов {@link models.MusicBand}
     */
    private final LinkedHashSet<MusicBand> list;
    /**
     * Создание истории команды {@code remove_greater}.
     *
     * @param commandName название команды
     * @param list список удалённых объектов
     */
    public HistoryRemoveGreater(String commandName, LinkedHashSet<MusicBand> list) {
        super(commandName);
        this.list = list;
    };
    /**
     * Получить {@code list}.
     *
     * @return Возвращает {@code list}
     */
    public LinkedHashSet<MusicBand> getList() {
        return list;
    }
}
