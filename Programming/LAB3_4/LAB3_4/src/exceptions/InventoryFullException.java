package exceptions;

public class InventoryFullException extends Exception {
    public InventoryFullException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "ИНВЕНТАРЬ ПЕРЕПОЛНЕН: " + super.getMessage();
    }
}
