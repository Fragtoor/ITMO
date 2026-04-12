package models;

import java.io.Serializable;

/**
 * Хранит сумму продаж лейбла
 */
public class Label implements Serializable {
    /**
     * Продажи лейбла.
     * Поле не может быть {@code null}, должно быть больше 0.
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
     *
     * @param sales продаже лейбла
     */
    public Label(Double sales) {
        this.sales = sales;
    }
    /**
     * Установить значение {@code sales}
     *
     * @param sales продаже лейбла
     */
    public void setSales(Double sales) {
        this.sales = sales;
    }
    public Double getSales() {
        return sales;
    }

    /**
     * Получить значение {@code sales}
     *
     * @return Возвращает {@code sales}
     */
    public boolean validate() {
        return (sales != null) && (sales > 0);
    }
}