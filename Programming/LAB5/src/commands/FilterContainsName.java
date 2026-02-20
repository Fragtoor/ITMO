package commands;

import exceptions.InvalidInputException;
import main_classes.Main;
import tools.CollectionManager;

/**
 * Реализует команду filter_contains_name, которая выводит элементы, значение поля name которых содержит заданную подстроку
 *
 * @author alexSIV
 * @version 1.0
 */
public class FilterContainsName extends Command{
    /**
     * Создает команду {@code filter_contains_name}.
     */
    public FilterContainsName(Object parameter) {
        super(parameter);
    }

    public void validate() {
        if (this.parameter == null) {
            throw new InvalidInputException("У filter_contains_name должен быть аргумент name!\n");
        }
    }

    public void execute() {
        Main.commandsList.add("filter_contains_name");
        CollectionManager.filterContainsName((String)this.parameter);
    }
}
