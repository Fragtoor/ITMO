package main_classes;

import exceptions.InvalidFileExtensionException;
import reader_manager.InputManager;
import tools.CollectionManager;
import tools.FileManager;

import java.io.FileNotFoundException;

/**
 * Точка входа в программу.
 */
public class Main {
    /**
     * Запускает программу.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Перезапустите программу и укажите путь к файлу!");
            System.exit(0);
        }
        try {
            if (!FileManager.fileExists(args[0])) {
                throw new FileNotFoundException("Укажите правильный путь к файлу!\n");
            }
            if (!FileManager.hasRightReadToRead(args[0])) {
                throw new FileNotFoundException("Нет прав на чтение файла!\n");
            }
            if (!FileManager.hasExtension(args[0], "csv")) {
                throw new InvalidFileExtensionException("Файл должен быть в формате csv!\n");
            }

            CollectionManager.help();
            ApplicationContext.FILE_NAME = args[0];
            ApplicationContext.collection = FileManager.readCollectionFromFile();
            InputManager.startInput();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }
}
