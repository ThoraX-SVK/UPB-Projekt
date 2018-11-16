package services.auth.interfaces;

import java.security.NoSuchAlgorithmException;

public interface PasswordSecurity {

    String createHashAndSaltString(String password) throws Exception;

    String hash(String password, String salt) throws NoSuchAlgorithmException;
}
