package managers;


public class TransactionManager {

    private int countCommand;

    private boolean active;

    public void beginTransaction() {
        countCommand = 0;
        active = true;
    }

    public void nextCommand() {
        countCommand++;
    }

    public int rollback() {
        if (!active) {
            return 0;
        }
        return countCommand;
    }
}
