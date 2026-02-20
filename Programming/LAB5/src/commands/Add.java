package commands;

import exceptions.InvalidInputException;
import main_classes.Main;
import models.MusicBand;
import tools.CollectionManager;
import models.MusicBandCreate;
/**
 * Реализует команду add, которая добавляет новый элемент в коллекцию
 *
 * @author alexSIV
 * @version 1.0
 */
public class Add extends Command {
    /**
     * Создает команду {@code add}.
     */
    public Add(Object parameter) {
        super(parameter);
    }

    public void execute() {
        Main.commandsList.add("add");
        CollectionManager.add();
    }
}
