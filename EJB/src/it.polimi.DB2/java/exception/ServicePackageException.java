package exception;

public class ServicePackageException extends Exception {
    public ServicePackageException(String message) {
        super(message == null ? "Offer not found" : message);
    }
}
