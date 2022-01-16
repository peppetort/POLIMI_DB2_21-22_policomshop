package exception;

public class OptionalProductException extends Exception{
    public OptionalProductException(String message) {
        super(message == null ? "Offer not found": message);
    }
}
