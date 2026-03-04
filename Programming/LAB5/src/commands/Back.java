package commands;

import exceptions.InvalidInputException;
import tools.CollectionManager;
import tools.Validator;

/**
 * Реализует команду {@code back n}, которая отменяет действия последних {@code n} команд.
 */
public class Back extends Command{
    /**
     * Создание команды {@code back}.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public Back(Object parameter) {
        super(parameter);
    }
    /**
     * Проверка значения параметра {@code n}, переданного команде {@code back}.
     *
     * <p>Аргумент {@code n} должен быть положительным целым числом типа {@code int}</p>
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
            throw new InvalidInputException("У back должен быть аргумент n - целое положительное число!\n");
        }
    }
    /**
     * Выполнение команды {@code back}.
     */
    public void execute() {
        CollectionManager.back(Integer.parseInt((String)this.parameter));
    }
}
