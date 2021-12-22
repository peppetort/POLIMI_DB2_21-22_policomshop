package exception;

public class OrderNotFound extends Exception{
    public OrderNotFound(String message) {
        super(message == null ? "Order not found": message);
    }
}
