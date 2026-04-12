package exceptions;
/**
 * Исключение, выбрасываемое при бесконечном рекурсивном выполнении скриптов с помощью команды {@code execute_script}.
 */
public class RecursiveCallException extends RuntimeException {
    /**
     * Создание исключения RecursiveCallException.
     *
     * @param message сообщение, переданное при срабатывании исключения
     */
    public RecursiveCallException(String message) {
        super(message);
    }
}
