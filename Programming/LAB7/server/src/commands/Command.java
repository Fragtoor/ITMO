package commands;

import common.general.Response;
import common.general.ResponseType;

/**
 * Класс-предок для всех команд.
 */
public class Command {
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
    public Response execute(Object... params) {
        return new Response(ResponseType.COMMAND_ERROR, "Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n");
    }
    public String getCommandName() {
        return "command";
    }
}
