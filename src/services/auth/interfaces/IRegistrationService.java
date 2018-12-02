package services.auth.interfaces;

import database.exceptions.UserAlreadyExistsException;

public interface IRegistrationService {

    void registerUser(String username, String password) throws Exception;
    void checkUsernameAvailable(String username) throws UserAlreadyExistsException;
}
