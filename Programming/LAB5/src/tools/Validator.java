package tools;
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
}
