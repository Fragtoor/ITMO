package server;

import common.general.Request;
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

    public String run(Object request) {
        if (!(request instanceof Request req)) return "Ошибка, неправильный формат запроса";

        String commandName = req.getCommandName();
        var argumentParam = req.getArgumentParam();
        var argumentObject = req.getArgumentObject();
        boolean fromTheFile = req.getFromTheFile();

        if (fromTheFile && tm == null) {
            tm = new TransactionManager();
            tm.beginTransaction();
        }
        if (!fromTheFile && tm != null) {tm = null;}
        try {
            sm.collectionManager.setCollection(FileManager.readCollectionFromFile(fileName));
            CommandManager commandManager = new CommandManager(sm);
            String result = commandManager.execute(commandName, argumentParam, argumentObject);
            FileManager.saveCollection(fileName, sm.collectionManager.getCollection());

            if (tm != null) tm.nextCommand();
            return result;
        } catch (Exception e) {
            String result = e.getMessage() + "\n";
            if (tm != null) {
                sm.collectionManager.back(tm.rollback());
                result += "Все изменения, вызванные командой execute_script, отменены";
                tm = null;
            }
            return result;
        }
    }
}
