package database.classes;

public class UserData {

    private String username;
    private String password;
    private String salt;

    public UserData(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public static UserData fromUserData(String username, String password, String salt) {
        return new UserData(username, password, salt);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

}
