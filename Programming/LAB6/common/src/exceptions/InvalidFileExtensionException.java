package exceptions;

import java.io.IOException;
/**
 * Исключение, выбрасываемое при вводе имени файла с некорректным расширением.
 */
public class InvalidFileExtensionException extends IOException {
    /**
     * Создание исключения InvalidFileExtensionException.
     *
     * @param message сообщение, переданное при срабатывании исключения
     */
    public InvalidFileExtensionException(String message) {
        super(message);
    }
}
