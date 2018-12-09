package services.auth;

import database.classes.UserData;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.UserAlreadyExistsException;
import services.auth.interfaces.IRegistrationService;
import services.auth.interfaces.PasswordSecurity;
import services.user.UserServiceImpl;
import services.user.interfaces.UserService;


public class RegistrationService implements IRegistrationService {

    private PasswordSecurity passwordSecurity = new PasswordSecurityImpl();
    private UserService userService = new UserServiceImpl();

    @Override
    public void registerUser(String username, String password) throws UserAlreadyExistsException, DatabaseNotLoadedException {
        String passwordAndSalt = passwordSecurity.createHashAndSaltString(password);
        String[] passwordAndSaltSplit = passwordAndSalt.split(":");
        UserData newUser = UserData.fromUserData(username, passwordAndSaltSplit[0], passwordAndSaltSplit[1]);
        userService.save(newUser);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        UserData found = userService.findByUsername(username);
        return found == null;
    }
}
