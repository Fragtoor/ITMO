package reader_manager;

import exceptions.InvalidInputException;
/**
 * Класс для парсинга и маршрутизации пользовательского ввода.
 *
 * @author alexSIV
 * @version 1.0
 */
public class Reader {
    /**
     * Обрабатывает введённую строку и отдаёт её на выполнение CommandManager{@link reader_manager.CommandManager}
     */
    public static void getLine(String read) {
        String[] line = read.split(" ");

        try {
            if (line.length == 0) {
                throw new InvalidInputException("Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n");

            } else if (line.length == 1) {
                toCommand(line[0].trim(), null);
            } else {
                toCommand(line[0].trim(), line[1].trim());
            }
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Запускает выполнение команды
     */
    private static void toCommand(String command, Object parameter) {
        CommandManager execCommand = new CommandManager(parameter);
        execCommand.runCommand(command);
    }
}
