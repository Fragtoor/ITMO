package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.BDManager;
import dao.DAO;
import managers.CollectionManager;

import java.sql.SQLException;


public class Back extends Command {
    private final CollectionManager cm;
    public Back(CollectionManager cm, User user, DAO dao) {
        super(user, dao);
        this.cm = cm;
    }

    public boolean validateParams(Object... params) {
        if (params.length == 0 || !(params[0] instanceof String n)) return false;
        int number;
        try {
            number = Integer.parseInt(n);
            if (number < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public Response execute(Object... params) {
        try {
            int n = Integer.parseInt((String)params[0]);
            String message = cm.back(n);
            for (int i = 0; i < n; i++) {
                BDManager.deleteHistoryCommand(getDAO(), getUser());
                cm.getCommandsList().pop();
            }
            return new Response(ResponseType.COMMAND_SUCCESS, message);
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке отменить команды\n");
        }
    }
    public String getCommandName() {
        return "back";
    }
}
