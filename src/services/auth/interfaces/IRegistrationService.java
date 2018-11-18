package services.auth.interfaces;

import database.exceptions.UserAlreadyExistsException;

public interface IRegistrationService {

    public void registerUser(String username, String password) throws Exception;
    public void checkUsernameAvailable(String username) throws UserAlreadyExistsException;
}
