package services.auth;

import database.UserRepository;
import database.classes.UserData;
import database.exceptions.UserAlreadyExistsException;
import services.auth.interfaces.IRegistrationService;
import services.auth.interfaces.PasswordSecurity;


public class RegistrationService implements IRegistrationService {

    private PasswordSecurity passwordSecurity = new PasswordSecurityImpl();
    private UserRepository userRepository = new UserRepository();

    @Override
    public void registerUser(String username, String password) throws Exception {

        try {
            String passwordAndSalt = passwordSecurity.createHashAndSaltString(password);
            userRepository.add(username, passwordAndSalt);

        } catch (UserAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public void checkUsernameAvailable(String username) throws UserAlreadyExistsException {
        UserData found = userRepository.find(username);
        if (found != null) {
            throw UserAlreadyExistsException.fromUsername(username);
        }
    }
}
