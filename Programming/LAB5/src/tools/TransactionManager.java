package tools;

import main_classes.ApplicationContext;
/**
 * Менеджер транзакций - реализует транзакционное выполнение скрипта.
 */
public class TransactionManager {
    /**
     * Индекс в {@code commandsList} последней команды перед {@code execute_script}
     */
    private int startCommandindex;
    /**
     * Статус начала транзакции
     */
    private boolean active;
    /**
     * Начинает транзакцию
     */
    public void beginTransaction() {
        startCommandindex = ApplicationContext.commandsList.size();
        this.active = true;
    }
    /**
     * Откат транзакции
     */
    public void rollback() {
        if (!active) {
            System.out.println("Нет активной транзакции для отката");
            return;
        }
        if (ApplicationContext.commandsList.size() - startCommandindex <= 0) return;
        CollectionManager.back(ApplicationContext.commandsList.size() - startCommandindex);
        this.active = false;
    }
}
