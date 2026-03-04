package history_commands;
/**
 * Хранит в себе имя команды {@code save}.
 */
public class HistorySave extends HistoryCommand {
    /**
     * История сохранённого в файл контента
     */
    private final String content;
    /**
     * Создание истории команды {@code save}.
     *
     * @param commandName название команды
     * @param content история сохранённого в файл контента
     */
    public HistorySave(String commandName, String content) {
        super(commandName);
        this.content = content;
    };

    public String getContent() {return content;}
}
