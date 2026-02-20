package models;
/**
 * Хранит сумму продаж лейбла
 *
 * @author alexSIV
 * @version 1.0
 */
public class Label {
    /**
     * Продажи лейбла.
     * Поле не может быть null, должно быть больше 0.
     */
    private Double sales;
    /**
     * Создание объекта {@link Label} без значения {@code sales}
     */
    public Label() {
        super();
    }
    /**
     * Создание объекта {@link Label} со значением {@code sales}
     */
    public Label(Double sales) {
        this.sales = sales;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }

    public Double getSales() {
        return sales;
    }
}