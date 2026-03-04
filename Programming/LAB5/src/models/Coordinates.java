package models;
/**
 * Хранит координату x и y
 */
public class Coordinates {
    /**
     * Координата X.
     */
    private int x;
    /**
     * Координата Y. Не может быть {@code null}
     */
    private Long y;
    /**
     * Создание объекта {@link Coordinates} без координат
     */
    public Coordinates() {
        super();
    }
    /**
     * Создание объекта {@link Coordinates} с координатами
     *
     * @param x координата {@code x}
     * @param y координата {@code y}
     */
    public Coordinates(int x, Long y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Получить координату {@code x}
     *
     * @return Возвращает {@code x}
     */
    public int getX() {
        return x;
    }
    /**
     * Получить координату {@code y}
     *
     * @return Возвращает {@code y}
     */
    public Long getY() {
        return y;
    }
    /**
     * Установить значение {@code x}
     *
     * @param x координата x
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * Установить значение {@code y}
     *
     * @param y координата y
     */
    public void setY(Long y) {
        this.y = y;
    }
}