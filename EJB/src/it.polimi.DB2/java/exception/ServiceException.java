package exception;

public class ServiceException extends Exception{
    public ServiceException(String message) {
        super(message == null ? "Offer not found": message);
    }
}
