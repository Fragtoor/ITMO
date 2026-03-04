package commands;

import exceptions.InvalidInputException;
import main_classes.ApplicationContext;
import models.MusicBand;
import history_commands.HistoryUpdate;
import tools.CollectionManager;
import tools.Validator;

/**
 * Реализует команду {@code update id}, которая обновляет значение элемента коллекции, {@code id} которого равен заданному.
 */
public class Update extends Command{
    /**
     * Создает команду {@code update}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public Update(Object parameter) {
        super(parameter);
    }
    /**
     * Проверка значения параметра {@code id}, переданного команде {@code update}.
     *
     * <p>Аргумент {@code id} должен быть положительным целым числом типа {@code int}</p>
     */
    public void validate() {
        try {
            if (this.parameter == null) {
                throw new InvalidInputException("");
            } else if (!Validator.isInt(this.parameter)) {
                throw new InvalidInputException("");
            } else if (Integer.parseInt((String)this.parameter) <= 0) {
                throw new InvalidInputException("");
            }
        } catch(InvalidInputException e){
            throw new InvalidInputException("У update должен быть аргумент id - целое положительное число типа int!\n");
        }
    }
    /**
     * Откат команды {@code update}.
     */
    public void undo() {
        int id = ((HistoryUpdate) ApplicationContext.commandsList.peek()).getId();
        MusicBand band = ((HistoryUpdate)ApplicationContext.commandsList.peek()).getBand();
        if (band == null) return;

        for (var elem: ApplicationContext.collection) {
            if (elem.getId() == id) {
                elem.setFields(band);
                break;
            }
        }
    }
    /**
     * Выполнение команды {@code update}.
     */
    public void execute() {
        MusicBand band = CollectionManager.update(Integer.parseInt((String)this.parameter));
        ApplicationContext.commandsList.add(new HistoryUpdate("update", Integer.parseInt((String)this.parameter), band));
    }
}
