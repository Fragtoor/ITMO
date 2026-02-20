package commands;

/**
 * Класс-предок для всех команд
 *
 * @author alexSIV
 * @version 1.0
 */
public class Command {
    /**
     * Параметр, которые передаётся команде.
     * Может быть как числом, так и строкой.
     */
    public Object parameter;
    /**
     * Создает команду без параметра.
     */
    public Command() {
        this.parameter = null;
    }
    /**
     * Создает команду с параметром.
     */
    public Command(Object parameter) {
        this.parameter = parameter;
    }
    /**
     * Проверка на валидность введённой команды
     */
    public void validate() {}
    /**
     * Выполнение команды
     */
    public void execute() {
        System.out.println("Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n");
    }
}
