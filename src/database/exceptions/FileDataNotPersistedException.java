package database.exceptions;

public class FileDataNotPersistedException extends Exception {

    public FileDataNotPersistedException(Throwable cause) {
        super(cause);
    }

    public static FileDataNotPersistedException generic(Throwable cause) {
        return new FileDataNotPersistedException(cause);
    }
}
