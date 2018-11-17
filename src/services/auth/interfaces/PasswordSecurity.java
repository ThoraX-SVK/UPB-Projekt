package services.auth.interfaces;


public interface PasswordSecurity {

    String createHashAndSaltString(String password);

    String hash(String password, String salt);
}
