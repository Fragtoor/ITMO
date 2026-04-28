package common.tools;

import common.exceptions.InvalidInputException;

/**
 * Класс, проверяющий соответствие некоторых значений установленным правилам.
 */
public class Validator {
    /**
     * Проверяет, является ли параметр {@code number} числом типа {@code int}.
     *
     * @param number значение, которое нужно проверить на соответствие типу {@code int}
     *
     * @return Возвращает {@code true}, если соответствует, иначе {@code false}
     */
    public static boolean isInt(Object number) {
        try {
            Integer.parseInt((String)number);
            String param = (String)number;
            long nCheck = Long.parseLong(param);
            if (param.trim().length() > 11 || nCheck > Integer.MAX_VALUE || nCheck < Integer.MIN_VALUE) {
                throw new NumberFormatException();
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void validatePassword(String password) throws InvalidInputException {
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        if (password == null || password.isBlank()) {
            throw new InvalidInputException("Пароль не может быть пустым");
        }

        if (password.length() < 8) {
            throw new InvalidInputException("Пароль должен содержать минимум 8 символов");
        }

        if (!password.matches(PASSWORD_PATTERN)) {
            throw new InvalidInputException("Пароль должен содержать цифры, заглавные и строчные буквы");
        }
    }
}
