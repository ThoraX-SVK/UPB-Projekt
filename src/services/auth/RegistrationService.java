package services.auth;

import database.Database;
import services.auth.interfaces.IRegistrationService;
import services.auth.interfaces.PasswordSecurity;

public class RegistrationService implements IRegistrationService {

    PasswordSecurity passwordSecurity = new PasswordSecurityImpl();

    @Override
    public void registerUser(String username, String password) throws Exception {

        try {
            String passwordAndSalt = passwordSecurity.createHashAndSaltString(password);
            Database.add(username, passwordAndSalt);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
}
