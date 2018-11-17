package database;

public class Result {

    private String username;
    private String passAndSalt;
    private String salt;

    public Result(String username, String passAndSalt) {
        this.username = username;
        this.passAndSalt = passAndSalt;
    }

    public static Result fromUsernameAndPassword(String username, String password) {
        return new Result(username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassAndSalt() {
        return passAndSalt;
    }

    public void setPassAndSalt(String passAndSalt) {
        this.passAndSalt = passAndSalt;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
