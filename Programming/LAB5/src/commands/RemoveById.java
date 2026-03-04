package commands;

import exceptions.InvalidInputException;
import main_classes.ApplicationContext;
import models.MusicBand;
import history_commands.HistoryRemoveById;
import tools.CollectionManager;
import tools.Validator;

/**
 * Реализует команду {@code remove_by_id id}, которая удаляет элемент из коллекции по его {@code id}.
 */
public class RemoveById extends Command{
    /**
     * Создает команду {@code remove_by_id}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public RemoveById(Object parameter) {
        super(parameter);
    }
    /**
     * Проверка значения параметра {@code id}, переданного команде {@code remove_by_id}.
     *
     * <p>Аргумент {@code id} должен быть положительным целым числом типа {@code int}</p>
     */
    public void validate() {
        try {
            if (getParameter() == null) {
                throw new InvalidInputException("");
            } else if (!Validator.isInt(getParameter())) {
                throw new InvalidInputException("");
            } else if (Integer.parseInt((String)getParameter()) <= 0) {
                throw new InvalidInputException("");
            }
        } catch(InvalidInputException e){
            throw new InvalidInputException("У remove_by_id должен быть аргумент id - целое положительное число!\n");
        }
    }
    /**
     * Откат команды {@code remove_by_id}.
     */
    public void undo() {
        int id = ((HistoryRemoveById) ApplicationContext.commandsList.peek()).getId();
        MusicBand band = ((HistoryRemoveById)ApplicationContext.commandsList.peek()).getBand();
        if (band == null) return;

        band.setId(id);
        ApplicationContext.collection.add(band);
    }
    /**
     * Выполнение команды {@code remove_by_id}.
     */
    public void execute() {
        MusicBand band = CollectionManager.removeById(Integer.parseInt((String)getParameter()));
        ApplicationContext.commandsList.add(new HistoryRemoveById("remove_by_id", Integer.parseInt((String)getParameter()), band));
    }
}
