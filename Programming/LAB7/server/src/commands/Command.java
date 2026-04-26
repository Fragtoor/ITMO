package commands;

import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.DAO;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Класс-предок для всех команд.
 */
public class Command implements Serializable {
    private User user;
    private DAO dao;

    public Command(User user, DAO dao) {
        this.user = user;
        this.dao = dao;
    }
    /**
     * Откат команды
     */
    public void undo() throws SQLException {}
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

    public User getUser() {return user;}

    public DAO getDAO() {return dao;}
}
