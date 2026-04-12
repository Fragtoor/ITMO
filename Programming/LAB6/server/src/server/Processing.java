package server;

import general.Request;
import tools.CollectionManager;
import tools.CommandManager;
import tools.FileManager;
import tools.TransactionManager;

public class Processing {
    private CollectionManager cm;
    private TransactionManager tm = null;
    private String fileName;
    public Processing(CollectionManager cm, String fileName) {
        this.cm = cm;
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
            cm.setCollection(FileManager.readCollectionFromFile(fileName));
            CommandManager commandManager = new CommandManager(cm);
            String result = commandManager.execute(commandName, argumentParam, argumentObject);
            FileManager.saveCollection(fileName, cm.getCollection());

            if (tm != null) tm.nextCommand();
            return result;
        } catch (Exception e) {
            String result = e.getMessage() + "\n";
            if (tm != null) {
                cm.back(tm.rollback());
                result += "Все изменения, вызванные командой execute_script, отменены";
                tm = null;
            }
            return result;
        }
    }
}
