package history_commands;
/**
 * Хранит в себе имя команды {@code show}.
 */
public class HistoryShow extends HistoryCommand {
    /**
     * Создание истории команды {@code show}.
     *
     * @param commandName название команды
     */
    public HistoryShow(String commandName) {
        super(commandName);
    };
}
