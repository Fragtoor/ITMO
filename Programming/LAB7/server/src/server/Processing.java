package server;

import common.general.Request;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import common.tools.FileManager;
import managers.*;

public class Processing {
    private ServerManagers sm;
    private TransactionManager tm = null;
    private String fileName;
    public Processing(ServerManagers sm, String fileName) {
        this.sm = sm;
        this.fileName = fileName;
    }

    public Response run(Object request) {
        if (!(request instanceof Request req)) return new Response(ResponseType.COMMAND_ERROR, "Ошибка, неправильный формат запроса");

        String commandName = req.getCommandName();
        var argumentParam = req.getArgumentParam();
        var argumentObject = req.getArgumentObject();
        boolean fromTheFile = req.getFromTheFile();
        User user = req.getUser();

        // Добавь сюда метод, который по id определяет коллекцию из бд
        // !!!
        // !!!

        if (fromTheFile && tm == null) {
            tm = new TransactionManager();
            tm.beginTransaction();
        }
        if (!fromTheFile && tm != null) {tm = null;}
        try {
            sm.collectionManager.setCollection(FileManager.readCollectionFromFile(fileName));
            CommandManager commandManager = new CommandManager(sm);
            Response response = commandManager.execute(commandName, argumentParam, argumentObject);
            FileManager.saveCollection(fileName, sm.collectionManager.getCollection());

            if (tm != null) tm.nextCommand();
            return response;
        } catch (Exception e) {
            String result = e.getMessage() + "\n";
            if (tm != null) {
                sm.collectionManager.back(tm.rollback());
                result += "Все изменения, вызванные командой execute_script, отменены";
                tm = null;
            }
            return new Response(ResponseType.COMMAND_ERROR, result);
        }
    }
}
