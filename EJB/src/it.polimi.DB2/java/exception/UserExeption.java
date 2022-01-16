package exception;

public class UserExeption extends Exception {

    public UserExeption() {
        super("User not found");
    }

    public UserExeption(String message) {
        super(message);
    }
}
