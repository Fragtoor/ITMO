package reader_manager;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.ui.ConsoleColors;

import java.util.Arrays;

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
            throw new InvalidInputException(ConsoleColors.RED + "Такой команды нет! Используйте команду help, чтобы посмотреть список команд" + ConsoleColors.RESET);
        }
        String commandName = line[0];
        String[] args = Arrays.copyOfRange(line, 1, line.length);
        return toCommand(commandName, args);
    }
    /**
     * Запускает выполнение команды
     *
     * @param command название команды
     * @param params параметр, переданный этой команде
     */
    private static CommandClient toCommand(String command, String[] params) {
        CommandManager execCommand = new CommandManager(params);
        return execCommand.getCommand(command);
    }
}
