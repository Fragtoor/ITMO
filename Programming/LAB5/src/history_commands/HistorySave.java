package history_commands;
/**
 * Хранит в себе имя команды {@code save}.
 */
public class HistorySave extends HistoryCommand {
    /**
     * Создание истории команды {@code save}.
     *
     * @param commandName название команды
     */
    public HistorySave(String commandName) {
        super(commandName);
    };
}
