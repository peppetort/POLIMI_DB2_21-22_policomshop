package exception;

public class UserNotFound extends Exception{
    public UserNotFound(String message) {
        super(message == null ? "User not found": message);
    }
}
