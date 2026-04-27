package commands;

import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.DAO;
import managers.CollectionManager;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Класс-предок для всех команд.
 */
public class Command implements Serializable {
    private User user;
    public Command(User user) {
        this.user = user;
    }
    /**
     * Откат команды
     */
    public void undo(CollectionManager cm, DAO dao) throws SQLException {}
    public boolean validateParams(Object... params) {
        return true;
    }
    /**
     * Выполнение команды
     */
    public Response execute(CollectionManager cm, DAO dao, Object... params) {
        return new Response(ResponseType.COMMAND_ERROR, "Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n");
    }
    public String getCommandName() {
        return "command";
    }

    public User getUser() {return user;}
}
