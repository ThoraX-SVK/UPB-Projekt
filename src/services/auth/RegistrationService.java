package services.auth;

import database.Database;
import database.exceptions.UserAlreadyExistsException;
import services.auth.interfaces.IRegistrationService;
import services.auth.interfaces.PasswordSecurity;

public class RegistrationService implements IRegistrationService {

    private PasswordSecurity passwordSecurity = new PasswordSecurityImpl();

    @Override
    public void registerUser(String username, String password) throws Exception {

        try {
            String passwordAndSalt = passwordSecurity.createHashAndSaltString(password);
            Database.add(username, passwordAndSalt);

        } catch (UserAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
}
