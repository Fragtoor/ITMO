package history_commands;

/**
 * Класс-предок всех классов, которые хранят изменения, совершённые соответствующими командами,
 * и данные, переданные им.
 */
public class HistoryCommand {
    /**
     * Название команды.
     */
    private final String commandName;
    /**
     * Создание истории.
     *
     * @param commandName название команды
     */
    public HistoryCommand(String commandName) {
        this.commandName = commandName;
    }
    /**
     * Получить название команды.
     *
     * @return Возвращает {@code commandName}
     */
    public String getCommandName() {
        return commandName;
    }
}
