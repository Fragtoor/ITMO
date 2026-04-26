package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import common.models.MusicBand;
import dao.BDManager;
import dao.DAO;
import managers.CollectionManager;

import java.sql.SQLException;
import java.util.LinkedHashSet;


public class Clear extends Command {
    private final CollectionManager cm;
    public LinkedHashSet<MusicBand> backupCollection;
    public Clear(CollectionManager cm, User user, DAO dao) {
        super(user, dao);
        this.cm = cm;
    }

    public void undo() {
        cm.setCollection(backupCollection);
    }
    public Response execute(Object... params) {
        backupCollection = new LinkedHashSet<>(cm.getCollection());
        try {
            BDManager.clearCollection(getDAO(), getUser());
            BDManager.saveHistoryCommand(getDAO(), getUser(), this);

            String message = cm.clear();
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, message);
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке очистить коллекцию\n");
        }

    }
    public String getCommandName() {
        return "clear";
    }
}
