package reader_manager;

import java.util.NoSuchElementException;
import java.util.Scanner;
/**
 * Менеджер ввода - управляет чтением пользовательского ввода из консоли.
 *
 * @author alexSIV
 * @version 1.0
 */
public class InputManager {
    /**
     * Сканер для чтения данных из стандартного потока ввода {@link System#in}.
     */
    public static Scanner consoleRead = new Scanner(System.in);

    // Сохраняем оригинальный Scanner
    private static Scanner originalScanner = consoleRead;

    /**
     * Запускает основной цикл чтения команд из консоли.
     */
    public static void startInput() {
        try {
            while(consoleRead.hasNextLine()) {
                String input = consoleRead.nextLine();
                Reader.getLine(input);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Ошибка чтения файла");
            System.exit(0);
        }
    }

    /**
     * Читает одну строку из консоли
     */
    public static String readInput() {
        if (consoleRead.hasNextLine()) {
            return consoleRead.nextLine();
        } else {
            return null;
        }
    }

    public static void setFileInput(Scanner fileScanner) {
        consoleRead = fileScanner;
    }

    public static void restoreConsoleInput() {
        consoleRead = originalScanner;
    }

}