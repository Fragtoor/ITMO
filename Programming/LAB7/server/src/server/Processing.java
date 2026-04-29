package server;

import common.general.Request;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.BDManager;
import dao.DAO;
import managers.*;

import java.sql.SQLException;

public class Processing {
    private final ServerManagers sm;
    private TransactionManager tm = null;
    private final DAO dao;
    public Processing(ServerManagers sm, DAO dao) {
        this.sm = sm;
        this.dao = dao;
    }

    public Response run(Object request) {
        if (!(request instanceof Request req)) return new Response(ResponseType.COMMAND_ERROR, "Ошибка, неправильный формат запроса");

        String commandName = req.getCommandName();
        var argumentParam = req.getArgumentParam();
        var argumentObject = req.getArgumentObject();
        boolean fromTheFile = req.getFromTheFile();
        User user = req.getUser();
        if (commandName.equals("login") || commandName.equals("register")) {
            CommandManager commandManager = new CommandManager(sm, dao, user);
            return commandManager.execute(commandName, argumentParam, argumentObject);
        }
        try {
            BDManager.getUserID(dao, user);
        } catch (SQLException e) {
            return new Response(ResponseType.AUTH_ERROR, "Вы не авторизованы");
        }
        try {
            BDManager.setAllDataCollection(dao, user, sm.collectionManager);
            BDManager.setCreationDateCollection(dao, sm.collectionManager);
            BDManager.setCommandsList(dao, user, sm.collectionManager);
        } catch (Exception e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка обработки данных из БД");
        }

        if (fromTheFile && tm == null) {
            tm = new TransactionManager();
            tm.beginTransaction();
        }
        if (!fromTheFile && tm != null) {tm = null;}
        try {
            CommandManager commandManager = new CommandManager(sm, dao, user);
            Response response = commandManager.execute(commandName, argumentParam, argumentObject);

            if (tm != null) tm.nextCommand();
            return response;
        } catch (Exception e) {
            String result = e.getMessage() + "\n";
            if (tm != null) {
                try {
                    sm.collectionManager.back(tm.rollback(), dao);
                } catch (SQLException e2) {}

                result += "Все изменения, вызванные командой execute_script, отменены";
                tm = null;
            }
            return new Response(ResponseType.COMMAND_ERROR, result);
        }
    }
}
