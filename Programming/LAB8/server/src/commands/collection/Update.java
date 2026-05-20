package commands.collection;

import commands.Command;
import common.exceptions.InvalidInputException;
import common.net.*;
import common.models.MusicBand;
import common.tools.Validator;
import dao.DBManager;
import managers.CollectionManager;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Update extends Command {
    public Update(User user) {
        super(user);
    }

    public void validateParams(Object... params) {
        if (params.length < 2) throw new InvalidInputException("Получено меньше, чем нужно, параметров");
        if (!(params[0] instanceof String n)) throw new InvalidInputException("id - не положительное целое число.");
        if (!Validator.isInt(n) || Integer.parseInt(n) <= 0) throw new InvalidInputException("id - не положительное целое число.");
        if (!(params[1] instanceof MusicBand band && band.validate())) throw new InvalidInputException("Получены некорректные или поврежденные данные объекта MusicBand.");
    }

    public String getRequiredPermission() {
        return "UPDATE_OWN";
    }

    public Response execute(CollectionManager cm, DBManager db, Object... params) {
        try {
            int targetId = Integer.parseInt((String)params[0]);
            MusicBand newBandData = (MusicBand) params[1];
            newBandData.setCreationDate(LocalDateTime.now());

            MusicBand existingBand = cm.getBand(targetId);
            if (existingBand == null) {
                db.saveHistoryCommand(getUser(), getCommandName());
                return new Response(ResponseType.COMMAND_ERROR, "Объект с id " + targetId + " не найден в коллекции.");
            }

            boolean canUpdateAll = db.getUserPermissions(getUser()).contains("UPDATE_ALL");

            if (existingBand.getOwnerId() == getUser().getId() || canUpdateAll) {
                db.updateItem(newBandData, targetId);
                cm.update(targetId, newBandData);
                db.saveHistoryCommand(getUser(), getCommandName());
                return new Response(ResponseType.COMMAND_SUCCESS, "Объект с id " + targetId + " был изменён.");
            }
            return new Response(ResponseType.COMMAND_ERROR, "Объект с id " + targetId + " создан не вами. У вас нет прав на его изменение.");
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке обновить элемент в базе данных.");
        }
    }
    public String getCommandName() {
        return "update";
    }
}
