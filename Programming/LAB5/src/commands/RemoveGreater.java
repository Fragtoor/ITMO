package commands;

import exceptions.InvalidInputException;
import main_classes.Main;
import models.MusicBand;
import tools.CollectionManager;
import models.MusicBandCreate;
/**
 * Реализует команду remove_greater, которая удаляет из коллекции все элементы, превышающие заданный
 *
 * @author alexSIV
 * @version 1.0
 */
public class RemoveGreater extends Command {
    /**
     * Создает команду {@code remove_greater}.
     */
    public RemoveGreater(Object parameter) {
        super(parameter);
    }

    public void execute() {
        Main.commandsList.add("remove_greater");
        MusicBand result = MusicBandCreate.create();
        CollectionManager.removeGreater(result);
    }
}
