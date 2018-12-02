package database.exceptions;

public class DatabaseNotLoadedException extends Exception {

    public DatabaseNotLoadedException(Throwable cause) {
        super(cause);
    }

    public static DatabaseNotLoadedException generic(Throwable cause) {
        return new DatabaseNotLoadedException(cause);
    }
}
