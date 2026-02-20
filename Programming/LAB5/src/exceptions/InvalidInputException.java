package exceptions;
/**
 * Исключение, выбрасываемое при некорректном пользовательском вводе.
 *
 * @author alexSIV
 * @version 1.0
 */
public class InvalidInputException extends RuntimeException {
    /**
     * Создание исключения InvalidInputException.
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
