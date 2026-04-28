package common.general;

import java.io.Serializable;

public enum ResponseType implements Serializable {
    AUTH_SUCCESS,      // Успешная авторизация
    AUTH_ERROR,        // Ошибка авторизации
    COMMAND_SUCCESS,   // Команда выполнена успешно
    COMMAND_ERROR,     // Ошибка при выполнении команды
    SERVER_ERROR       // Внутренняя ошибка сервера
}
