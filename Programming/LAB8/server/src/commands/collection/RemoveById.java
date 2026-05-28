package commands.collection;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.net.*;
import common.models.MusicBand;
import common.tools.Validator;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;


public class RemoveById extends Command {
    public RemoveById(User user) {
        super(user);
    }

    public void validateParams(Object... params) throws InvalidInputException {
        if (params.length == 0 || !(params[0] instanceof String n)) throw new InvalidInputException("id - не положительное целое число.");
        if (!Validator.isInt(n) || Integer.parseInt(n) <= 0) throw new InvalidInputException("id - не положительное целое число.");
    }

    public String getRequiredPermission() {
        return "DELETE_OWN";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        int numberId = Integer.parseInt((String)params[0]);
        try {
            MusicBand band = cm.getBand(numberId);
            if (band == null) {
                return new Response.Builder(ResponseType.COMMAND_SUCCESS)
                        .message("server.error.id_not_found::" + numberId).build();
            }

            boolean canDeleteAll = db.permissions().getUserPermissions(getUser()).contains("DELETE_ALL");
            if (canDeleteAll || band.getOwnerId() == getUser().getId()) {
                db.collection().deleteItem(numberId);
                cm.removeById(numberId);
                db.history().saveHistoryCommand(getUser(), getCommandName());
                return new Response.Builder(ResponseType.COMMAND_SUCCESS)
                        .message("server.command.remove_by_id.success::" + numberId).build();
            }

            return new Response.Builder(ResponseType.COMMAND_ERROR)
                    .message("server.error.not_owner::" + numberId).build();
        } catch (SQLException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("server.error.db_error").build();
        }
    }

    public String getCommandName() {
        return "remove_by_id";
    }
}
