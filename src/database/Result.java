package database;

public class Result {

    private String username;
    private String password;
    private String salt;

    public Result(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public static Result fromUserData(String username, String password, String salt) {
        return new Result(username, password, salt);
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
