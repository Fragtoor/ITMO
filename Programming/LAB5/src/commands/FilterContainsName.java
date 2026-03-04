package commands;

import exceptions.InvalidInputException;
import history_commands.HistoryFilterContainsName;
import main_classes.ApplicationContext;
import tools.CollectionManager;

/**
 * Реализует команду {@code filter_contains_name name}, которая выводит элементы, значение поля {@code name} которых содержит заданную подстроку.
 */
public class FilterContainsName extends Command{
    /**
     * Создает команду {@code filter_contains_name}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public FilterContainsName(Object parameter) {
        super(parameter);
    }
    /**
     * Проверка значения параметра {@code name}, переданного команде {@code filter_contains_name}.
     *
     * <p>Аргумент {@code name} не должен быть {@code null}</p>
     */
    public void validate() {
        if (getParameter() == null) {
            throw new InvalidInputException("У filter_contains_name должен быть аргумент name!\n");
        }
    }
    /**
     * Выполнение команды {@code filter_contains_name}.
     */
    public void execute() {
        ApplicationContext.commandsList.add(new HistoryFilterContainsName("filter_contains_name"));
        CollectionManager.filterContainsName((String)getParameter());
    }
}
