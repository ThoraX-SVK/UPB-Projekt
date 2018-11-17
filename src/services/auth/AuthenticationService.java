package services.auth;

import database.Database;
import database.Result;
import services.auth.interfaces.IAuthenticationService;
import services.auth.interfaces.PasswordSecurity;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationService implements IAuthenticationService {

    private PasswordSecurity passwordSecurity = new PasswordSecurityImpl();

    @Override
    public boolean authenticateFromRequest(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (checkPasswordMatches(username, password)) {
            String cookie = CookieAuthorization.createNewCookieString();
            request.getSession().setAttribute("loginCookie", cookie);
            request.getSession().setAttribute("username", username);
            CookieAuthorization.addCookie(username, cookie);

            return true;
        }
        return false;
    }

    private boolean checkPasswordMatches(String username, String password) {
        Result foundUser = Database.find(username);
        if (foundUser == null)
            return false;

        String hashedPassword = passwordSecurity.hash(password, foundUser.getSalt());
        return hashedPassword.equals(foundUser.getPassword());
    }
}
