package commands;

/**
 * Класс-предок для всех команд.
 */
public class Command {
    /**
     * Параметр, который передаётся команде.
     * Может быть как числом, так и строкой.
     */
    private Object parameter;
    /**
     * Создает команду без параметра.
     */
    public Command() {
        this.parameter = null;
    }
    /**
     * Создает команду с параметром.
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public Command(Object parameter) {
        this.parameter = parameter;
    }
    /**
     * Проверка на валидность введённой команды
     */
    public void validate() {}
    /**
     * Откат команды
     */
    public void undo() {}
    /**
     * Выполнение команды
     */
    public void execute() {
        System.out.println("Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n");
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }
}
