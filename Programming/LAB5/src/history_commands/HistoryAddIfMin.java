package history_commands;
/**
 * Хранит в себе имя команды {@code add_if_min}, статус добавления объекта в коллекцию {@code collection}.
 */
public class HistoryAddIfMin extends HistoryCommand {
    /**
     * Статус добавления объекта в коллекцию {@code collection}.
     */
    private final boolean result;
    /**
     * Создание истории команды {@code add_if_min}.
     *
     * @param commandName название команды
     */
    public HistoryAddIfMin(String commandName, boolean result) {
        super(commandName);
        this.result = result;
    }
    /**
     * Получить {@code result}.
     *
     * @return Возвращает {@code result}
     */
    public boolean isResult() {
        return result;
    }
}
