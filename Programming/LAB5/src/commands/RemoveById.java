package commands;

import exceptions.InvalidInputException;
import tools.CollectionManager;

/**
 * Реализует команду remove_by_id, которая удаляет элемент из коллекции по его id
 *
 * @author alexSIV
 * @version 1.0
 */
public class RemoveById extends Command{
    /**
     * Создает команду {@code remove_by_id}.
     */
    public RemoveById(Object parameter) {
        super(parameter);
    }

    public void validate() {
        if (this.parameter == null) {
            throw new InvalidInputException("У remove_by_id должен быть аргумент id - целое положительное число!");
        } else {
            try {
                Integer.parseInt((String)this.parameter);
            } catch (InvalidInputException | NumberFormatException e) {
                throw new InvalidInputException("У remove_by_id должен быть аргумент id - целое положительное число!\n");
            }
        }
    }

    public void execute() {
        main_classes.Main.commandsList.add("remove_by_id");
        CollectionManager.removeById(Integer.parseInt((String)this.parameter));
    }
}
