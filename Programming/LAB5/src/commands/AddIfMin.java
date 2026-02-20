package commands;

import exceptions.InvalidInputException;
import models.MusicBand;
import tools.CollectionManager;
import models.MusicBandCreate;
/**
 * Реализует команду add_if_min, которая добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
 *
 * @author alexSIV
 * @version 1.0
 */
public class AddIfMin extends Command {
    /**
     * Создает команду {@code add_if_min}.
     */
    public AddIfMin(Object parameter) {
        super(parameter);
    }

    public void execute() {
        main_classes.Main.commandsList.add("add_if_min");
        MusicBand result = MusicBandCreate.create();
        CollectionManager.addIfMin(result);
    }
}
