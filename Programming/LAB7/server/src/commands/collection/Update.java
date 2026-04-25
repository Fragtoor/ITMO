package commands.collection;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import common.models.MusicBand;
import managers.CollectionManager;

public class Update extends Command {
    private int idUpdate;
    private MusicBand bandUpdate;
    private final CollectionManager cm;
    public Update(CollectionManager cm) {
        this.cm = cm;
    }

    public void undo() {
        if (bandUpdate == null) return;

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
        MusicBand band = (MusicBand) params[1];
        MusicBand newBand = cm.update(Integer.parseInt((String)params[0]), band);
        idUpdate = Integer.parseInt((String)params[0]);
        bandUpdate = newBand;
        cm.addToCommandsList(this);
        if (bandUpdate == null) return new Response(ResponseType.COMMAND_SUCCESS, "Объект с указанным id не найден.\n");
        return new Response(ResponseType.COMMAND_SUCCESS,"Объект с id " + params[0] + " был изменён.\n");
    }
    public String getCommandName() {
        return "update";
    }
}
