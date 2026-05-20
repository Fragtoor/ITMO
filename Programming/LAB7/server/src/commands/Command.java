package commands;

import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.io.Serializable;

/**
 * Класс-предок для всех команд.
 */
public class Command implements Serializable {
    private final User user;
    public Command(User user) {
        this.user = user;}
    public void validateParams(Object... params) {}

    public String getRequiredPermission() {
        return null;
    }
    /**
     * Выполнение команды
     */
    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        return new Response(ResponseType.COMMAND_ERROR, "Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n");
    }

    public Response execute(DBManager db, Object... params) {
        return new Response(ResponseType.COMMAND_ERROR, "Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n");
    }
    public String getCommandName() {
        return "command";
    }

    public User getUser() {return user;}
}
