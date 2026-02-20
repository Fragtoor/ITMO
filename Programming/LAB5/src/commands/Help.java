package commands;

import exceptions.InvalidInputException;

/**
 * Реализует команду help, которая выводит справку по доступным командам
 *
 * @author alexSIV
 * @version 1.0
 */
public class Help extends Command {
    /**
     * Справка по доступным командам
     */
    private final String helpMessage = "- help : получить справку по доступным командам\n" +
            "- info : получить информацию о коллекции (тип, дата инициализации, количество элементов)\n" +
            "- show : получить все элементы коллекции в строковом представлении\n" +
            "- add : добавить новый элемент в коллекцию\n" +
            "- update id : обновить значение элемента коллекции, id которого равен заданному\n" +
            "- remove_by_id id : удалить элемент из коллекции по его id\n" +
            "- clear : очистить коллекцию\n" +
            "- save : сохранить коллекцию в файл\n" +
            "- execute_script file_name : считать и исполнить скрипт из указанного файла\n" +
            "- exit : завершить программу (без сохранения в файл)\n" +
            "- add_if_min : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
            "- remove_greater : удалить из коллекции все элементы, превышающие заданный\n" +
            "- history : вывести последние 10 команд (без их аргументов)\n" +
            "- sum_of_number_of_participants : вывести сумму значений поля numberOfParticipants для всех элементов коллекции\n" +
            "- average_of_number_of_participants : вывести среднее значение поля numberOfParticipants для всех элементов коллекции\n" +
            "- filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку\n";
    /**
     * Создает команду {@code help}.
     *
     * @param parameter параметр команды (для help должен быть null)
     */
    public Help (Object parameter) {
        super(parameter);
    }

    public void execute() {
        main_classes.Main.commandsList.add("help");
        System.out.println(helpMessage);
    }
}
