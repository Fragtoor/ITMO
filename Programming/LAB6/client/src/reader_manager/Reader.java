package reader_manager;

import commands.CommandClient;
import exceptions.InvalidInputException;
/**
 * Класс для парсинга и маршрутизации пользовательского ввода.
 */
public class Reader {
    /**
     * Обрабатывает введённую строку и отдаёт её на выполнение CommandManager{@link CommandManager}
     *
     * @param read строка, в которой должна быть команда
     */
    public static CommandClient getLine(String read) {
        String[] line = read.split(" ");
        if (line.length == 0) {
            throw new InvalidInputException("Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n");
        } else if (line.length == 1) {
            return toCommand(line[0].trim(), null);
        } else {
            return toCommand(line[0].trim(), line[1].trim());
        }
    }
    /**
     * Запускает выполнение команды
     *
     * @param command название команды
     * @param parameter параметр, переданный этой команде
     */
    private static CommandClient toCommand(String command, Object parameter) {
        CommandManager execCommand = new CommandManager(parameter);
        return execCommand.getCommand(command);
    }
}
