package history_commands;
/**
 * Хранит в себе имя команды {@code clear}.
 */
public class HistoryClear extends HistoryCommand {
    /**
     * Создание истории команды {@code clear}.
     *
     * @param commandName название команды
     */
    public HistoryClear(String commandName) {
        super(commandName);
    };
}
