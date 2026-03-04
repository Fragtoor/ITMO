package main_classes;

import history_commands.HistoryCommand;
import models.MusicBand;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Stack;
/**
 * Содержит основные статические поля приложения.
 */
public class ApplicationContext {
    /**
     * Дата инициализации коллекции в формате YYYY-MM-DDTHH:MM:SS.SSS
     */
    public static LocalDateTime time = LocalDateTime.now();
    /**
     * История введённых пользователем команд
     */
    public static Stack<HistoryCommand> commandsList = new Stack<>();
    /**
     * Коллекция объектов типа {@link MusicBand}
     */
    public static LinkedHashSet<MusicBand> collection;
    /**
     * Копия коллекции {@code collection}
     */
    public static LinkedHashSet<MusicBand> backupCollection;
    /**
     * Путь до файла, в котором содержаться данные для начального заполнения коллекции {@code collection}
     */
    public static String FILE_NAME;
}
