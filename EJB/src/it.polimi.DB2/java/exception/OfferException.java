package exception;

public class OfferException extends Exception{
    public OfferException(String message) {
        super(message == null ? "Offer not found": message);
    }
}
