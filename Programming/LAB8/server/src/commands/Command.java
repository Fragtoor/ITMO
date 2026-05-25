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
        return new Response.Builder(ResponseType.COMMAND_ERROR).message("Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n").build();
    }

    public Response execute(DBManager db, Object... params) {
        return new Response.Builder(ResponseType.COMMAND_ERROR).message("Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n").build();
    }
    public String getCommandName() {
        return "command";
    }

    public User getUser() {return user;}
}
