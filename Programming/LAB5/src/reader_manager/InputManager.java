package reader_manager;

import java.util.NoSuchElementException;
import java.util.Scanner;
/**
 * Менеджер ввода - управляет чтением пользовательского ввода из консоли.
 */
public class InputManager {
    /**
     * Сканер для чтения данных из стандартного потока ввода {@link System#in}.
     */
    public static Scanner consoleRead = new Scanner(System.in);
    /**
     * Оригинальный сканер для чтения данных из стандартного потока ввода {@link System#in}.
     */
    private static final Scanner originalScanner = consoleRead;
    /**
     * Флаг, указывающий, что читаем из файла
     */
    private static boolean readingFromFile = false;
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
     *
     * @return прочитанная строка, или {@code null}, если достигнут конец файла
     * (при чтении из файла) или если ввод отсутствует
     */
    public static String readInput() {
        if (consoleRead.hasNextLine()) {
            return consoleRead.nextLine();
        } else {
            return null;
        }
    }
    /**
     * Установить значение сканера.
     *
     * @param fileScanner сканер, связанный с файловым потоком ввода
     */
    public static void setFileInput(Scanner fileScanner) {
        readingFromFile = true;
        consoleRead = fileScanner;
    }
    /**
     * Вернуть сканер к стандартному для чтения из стандартного потока.
     */
    public static void restoreConsoleInput() {
        readingFromFile = false;
        consoleRead = originalScanner;
    }
    /**
     * Проверяет, достигнут ли конец файла (только при чтении из файла).
     *
     * @return true если файл закончился и читаем из файла
     */
    public static boolean isEndOfFile() {
        return readingFromFile && !consoleRead.hasNextLine();
    }


}