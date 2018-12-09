package services.auth.interfaces;

import database.exceptions.UserAlreadyExistsException;
import database.exceptions.DatabaseNotLoadedException;

public interface IRegistrationService {

    void registerUser(String username, String password) throws UserAlreadyExistsException, DatabaseNotLoadedException;
    boolean isUsernameAvailable(String username);
}
