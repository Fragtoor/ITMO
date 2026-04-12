package exceptions;
/**
 * Исключение, выбрасываемое при некорректном пользовательском вводе.
 */
public class InvalidInputException extends RuntimeException {
    /**
     * Создание исключения InvalidInputException.
     *
     * @param message сообщение, переданное при срабатывании исключения
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
