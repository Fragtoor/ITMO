package commands;

import exceptions.InvalidInputException;
import main_classes.Main;
import tools.CollectionManager;

/**
 * Реализует команду update, которая обновляет значение элемента коллекции, id которого равен заданному
 *
 * @author alexSIV
 * @version 1.0
 */
public class Update extends Command{
    /**
     * Создает команду {@code update}.
     */
    public Update(Object parameter) {
        super(parameter);
    }

    public void validate() {
        if (this.parameter == null) {
            throw new InvalidInputException("У update должен быть аргумент id - целое положительное число!");
        } else {
            try {
                Integer.parseInt((String)this.parameter);
            } catch (InvalidInputException | NumberFormatException e) {
                throw new InvalidInputException("У update должен быть аргумент id - целое положительное число!\n");
            }
        }
    }

    public void execute() {
        Main.commandsList.add("update");
        CollectionManager.update((String)this.parameter);
    }
}
