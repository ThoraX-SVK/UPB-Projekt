package handlers.auth.exceptions;

public class PasswordMismatchException extends Exception {

    public PasswordMismatchException() {
    }

    public PasswordMismatchException(String message) {
        super(message);
    }

    public static PasswordMismatchException generic() {
        return new PasswordMismatchException("Passwords do not match.");
    }
}
