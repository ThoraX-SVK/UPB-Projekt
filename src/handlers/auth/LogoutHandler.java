package handlers.auth;

import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/logout.jsp";
    private static final String HOME = "templates/homepage.jsp";

    private static final String LOGIN_PATH = "/login";

    // TODO: logout
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("username");
        CookieAuthorization.invalidateCookie(user);

        response.sendRedirect(UrlUtils.getUrlFromRequest(request) + LOGIN_PATH);
    }
}
