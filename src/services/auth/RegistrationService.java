package services.auth;

import database.UserRepository;
import database.classes.UserData;
import database.exceptions.UserAlreadyExistsException;
import services.auth.interfaces.IRegistrationService;
import services.auth.interfaces.PasswordSecurity;
import services.user.UserServiceImpl;
import services.user.interfaces.UserService;


public class RegistrationService implements IRegistrationService {

    private PasswordSecurity passwordSecurity = new PasswordSecurityImpl();
    private UserService userService = new UserServiceImpl();

    @Override
    public void registerUser(String username, String password) throws Exception {

        try {
            String passwordAndSalt = passwordSecurity.createHashAndSaltString(password);
            String[] passwordAndSaltSplit = passwordAndSalt.split(":");
            UserData newUser = UserData.fromUserData(username, passwordAndSaltSplit[0], passwordAndSaltSplit[1]);
            userService.save(newUser);

        } catch (UserAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void checkUsernameAvailable(String username) throws UserAlreadyExistsException {
        UserData found = userService.findByUsername(username);
        if (found != null) {
            throw UserAlreadyExistsException.fromUsername(username);
        }
    }
}
