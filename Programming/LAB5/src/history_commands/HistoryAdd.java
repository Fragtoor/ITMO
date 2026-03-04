package history_commands;
/**
 * Хранит в себе имя команды {@code add}.
 */
public class HistoryAdd extends HistoryCommand {
    /**
     * Создание истории команды {@code add}.
     *
     * @param commandName название команды
     */
    public HistoryAdd(String commandName) {
        super(commandName);
    };
}
