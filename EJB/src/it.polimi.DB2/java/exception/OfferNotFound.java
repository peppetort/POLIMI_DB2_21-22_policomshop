package exception;

public class OfferNotFound extends Exception{
    public OfferNotFound(String message) {
        super(message == null ? "Offer not found": message);
    }
}
