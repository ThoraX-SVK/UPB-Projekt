package handlers.auth;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

public class CookieAuthorization {

    private static final HashMap<String, String> cookies = new HashMap<>();

    public static void addCookie(String user, String cookie) {
        cookies.put(user, cookie);
    }

    public static boolean isNotLoggedIn(HttpServletRequest request) {
        String userName = (String) request.getSession().getAttribute("username");
        String cookie = (String) request.getSession().getAttribute("loginCookie");

        return !(userName != null && cookie != null && cookies.containsKey(userName) && cookies.get(userName).equals(cookie));
    }

    public static String createNewCookieString() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] cookieBytes = new byte[128];
        secureRandom.nextBytes(cookieBytes);
        return Base64.getEncoder().encodeToString(cookieBytes);
    }

    public static void invalidateCookie(String user) {
        cookies.remove(user);
    }
}
