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
import java.time.LocalDateTime;

public class Update extends Command {
    private int idUpdate;
    private MusicBand bandUpdate;
    private final CollectionManager cm;
    public Update(CollectionManager cm, User user, DAO dao) {
        super(user, dao);
        this.cm = cm;
    }

    public void undo() throws SQLException {
        if (bandUpdate == null) return;

        BDManager.updateItem(getDAO(), getUser(), bandUpdate, idUpdate);
        cm.getCollection().stream()
                .filter(elem -> elem.getId() == idUpdate)
                .findFirst()
                .ifPresent(elem -> elem.setFields(bandUpdate));
    }

    public boolean validateParams(Object... params) {
        if (params.length < 2) return false;
        if (!(params[0] instanceof String n)) return false;
        int number;
        try {
            number = Integer.parseInt(n);
            if (number < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            return false;
        }

        return params[1] instanceof MusicBand band && band.validate();
    }

    public Response execute(Object... params) {
        try {
            MusicBand band = (MusicBand) params[1];
            band.setCreationDate(LocalDateTime.now());
            BDManager.updateItem(getDAO(), getUser(), band, Integer.parseInt((String)params[0]));
            BDManager.saveHistoryCommand(getDAO(), getUser(), this);

            MusicBand oldBand = cm.update(Integer.parseInt((String)params[0]), band);
            idUpdate = Integer.parseInt((String)params[0]);
            bandUpdate = oldBand;
            cm.addToCommandsList(this);
            if (bandUpdate == null) return new Response(ResponseType.COMMAND_SUCCESS, "Объект с указанным id не найден.\n");
            return new Response(ResponseType.COMMAND_SUCCESS,"Объект с id " + params[0] + " был изменён.\n");
        } catch (SQLException e) {
            return new Response(ResponseType.COMMAND_ERROR, "Ошибка при попытке обновить элемент\n");
        }

    }
    public String getCommandName() {
        return "update";
    }
}
