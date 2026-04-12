package commands;

import tools.CollectionManager;

/**
 * Класс-предок для всех команд.
 */
public class Command {
    protected CollectionManager cm;
    public Command(CollectionManager cm) {
        this.cm = cm;
    }
    /**
     * Откат команды
     */
    public void undo() {}
    public boolean validateParams(Object... params) {
        return true;
    }
    /**
     * Выполнение команды
     */
    public String execute(Object... params) {
        return "Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n";
    }
    public String getCommandName() {
        return "command";
    }
}
