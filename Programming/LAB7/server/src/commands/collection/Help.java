package commands.collection;

import commands.Command;
import common.net.*;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class Help extends Command {
    public Help(User user) {
        super(user);
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {

        String[] result = cm.help();
        try {
            db.saveHistoryCommand(getUser(), this);
            cm.addToCommandsList(this);
            return new Response(ResponseType.COMMAND_SUCCESS, result[0], result[1]);
        } catch (SQLException e) {
            return new Response(ResponseType.SERVER_ERROR, "Ошибка на стороне сервера при попытке сохранить историю" + e.getMessage());
        }
    }
    public String getCommandName() {
        return "help";
    }
}
