package main_classes;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import models.MusicBand;
import reader_manager.InputManager;
import tools.CSVReader;
/**
 * Содержит точку входа в программу и основные статические поля.
 *
 * @author alexSIV
 * @version 1.0
 */
public class Main {
    /**
     * Дата инициализации коллекции в формате
     */
    public static LocalDateTime time = LocalDateTime.now();
    /**
     * История введённых пользователем команд
     */
    public static ArrayList<String> commandsList = new ArrayList<>();
    /**
     * Коллекция объектов типа {@link MusicBand}
     */
    public static LinkedHashSet<MusicBand> collection;
    /**
     * Путь до файла, в котором содержаться данные для начального заполнения коллекции collection
     */
    public static String FILE_NAME;

    /**
     * Старт программы
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Перезапустите программу и укажите путь к файлу!");
            System.exit(0);
        }
        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println("Укажите правильный путь к файлу!");
            System.exit(0);
        }
        FILE_NAME = args[0];
        collection = CSVReader.read();
        InputManager.startInput();
    }
}
