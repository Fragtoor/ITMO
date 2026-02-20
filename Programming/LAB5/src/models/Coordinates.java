package models;
/**
 * Хранит координату x и y
 *
 * @author alexSIV
 * @version 1.0
 */
public class Coordinates {
    /**
     * Координата X.
     */
    private int x;
    private Long y; //Поле не может быть null
    /**
     * Создание объекта {@link Coordinates} без координат
     */
    public Coordinates() {
        super();
    }
    /**
     * Создание объекта {@link Coordinates} с координатами
     */
    public Coordinates(int x, Long y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public Long getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(Long y) {
        this.y = y;
    }
}