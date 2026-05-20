package common.models;

import java.io.Serializable;

/**
 * Хранит координату x и y
 */
public class Coordinates implements Serializable {
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

    public boolean validate() {
        return y != null;
    }

    public int getX() {
        return x;
    }

    public Long getY() {
        return y;
    }
}