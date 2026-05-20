package reader_manager;

import commands.CommandClient;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Менеджер ввода - управляет чтением пользовательского ввода из консоли и файлов.
 */
public class InputManager {
    /**
     * Стандартный сканер для чтения данных из консоли.
     */
    private static final Scanner consoleScanner = new Scanner(System.in, StandardCharsets.UTF_8);

    /**
     * Стек сканеров для поддержки вложенных скриптов (execute_script внутри execute_script).
     */
    private static final Stack<Scanner> fileScanners = new Stack<>();

    /**
     * Запускает основной цикл чтения команд.
     */
    public static CommandClient getCommand() {
        try {
            String input = readInput();
            if (input != null) return Reader.getLine(input);
            return null;
        } catch (NoSuchElementException e) {
            System.out.println("Ошибка чтения файла");
            System.exit(0);
        }
        return null;
    }

    /**
     * Читает одну строку. Если есть активные файловые сканеры — читает из верхнего.
     * Если стек пуст — читает из консоли.
     */
    public static String readInput() {
        if (!fileScanners.isEmpty()) {
            Scanner currentScanner = fileScanners.peek();
            if (currentScanner.hasNextLine()) {
                return currentScanner.nextLine();
            } else {
                return null;
            }
        } else {
            if (consoleScanner.hasNextLine()) {
                return consoleScanner.nextLine();
            } else {
                return null;
            }
        }
    }

    /**
     * Добавляет новый файловый сканер поверх текущего (для вложенных скриптов).
     *
     * @param fileScanner сканер, связанный с файловым потоком ввода
     */
    public static void setFileInput(Scanner fileScanner) {
        fileScanners.push(fileScanner);
    }

    /**
     * Убирает завершенный файловый сканер. Если стек опустеет, чтение вернется к консоли.
     */
    public static void restoreConsoleInput() {
        if (!fileScanners.isEmpty()) {
            fileScanners.pop();
        }
    }

    /**
     * Проверяет, достигнут ли конец ТЕКУЩЕГО читаемого файла.
     */
    public static boolean isEndOfFile() {
        return !fileScanners.isEmpty() && !fileScanners.peek().hasNextLine();
    }

    /**
     * Возвращает true, если в данный момент идет чтение из файла.
     */
    public static boolean getReadingFromFile() {
        return !fileScanners.isEmpty();
    }

    public static void clearAllScanners() {
        fileScanners.clear();
    }
}