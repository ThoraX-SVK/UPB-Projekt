package database;

public class Result {

    private String username;
    private String password;
    private String salt;

    public Result(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static Result fromUsernameAndPassword(String username, String password) {
        return new Result(
                username,
                password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
