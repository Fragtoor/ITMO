package server;

import common.general.Request;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import common.tools.FileManager;
import dao.BDManager;
import dao.DAO;
import managers.*;

import java.sql.SQLException;

public class Processing {
    private final ServerManagers sm;
    private TransactionManager tm = null;
    private final String fileName;
    private final DAO dao;
    public Processing(ServerManagers sm, DAO dao, String fileName) {
        this.sm = sm;
        this.fileName = fileName;
        this.dao = dao;
    }

    public Response run(Object request) {
        if (!(request instanceof Request req)) return new Response(ResponseType.COMMAND_ERROR, "Ошибка, неправильный формат запроса");

        String commandName = req.getCommandName();
        var argumentParam = req.getArgumentParam();
        var argumentObject = req.getArgumentObject();
        boolean fromTheFile = req.getFromTheFile();
        User user = req.getUser();

        try {
            BDManager.setDataCollection(dao, user, sm.collectionManager);
            BDManager.setCreationDateCollection(dao, user, sm.collectionManager);
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
            sm.collectionManager.setCollection(FileManager.readCollectionFromFile(fileName));
            CommandManager commandManager = new CommandManager(sm, dao);
            Response response = commandManager.execute(commandName, argumentParam, argumentObject);
            FileManager.saveCollection(fileName, sm.collectionManager.getCollection());

            if (tm != null) tm.nextCommand();
            return response;
        } catch (Exception e) {
            String result = e.getMessage() + "\n";
            if (tm != null) {
                try {
                    sm.collectionManager.back(tm.rollback());
                } catch (SQLException e2) {}

                result += "Все изменения, вызванные командой execute_script, отменены";
                tm = null;
            }
            return new Response(ResponseType.COMMAND_ERROR, result);
        }
    }
}
