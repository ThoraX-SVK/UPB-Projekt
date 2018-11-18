package handlers.auth;

import services.auth.AuthenticationService;
import services.auth.CookieAuthorization;
import services.auth.RegistrationService;
import services.auth.interfaces.IAuthenticationService;
import services.auth.interfaces.IRegistrationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/login.jsp";
    private static final String HOME = "templates/homepage.jsp";

    private IAuthenticationService authenticationService = new AuthenticationService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            boolean isAuthenticated = authenticationService.authenticateFromRequest(request);

            if (isAuthenticated) {
                request.getRequestDispatcher(HOME).forward(request, response);
            } else {
                request.setAttribute("message", "Incorrect credentials!");
                request.setAttribute("username", request.getParameter("username"));
                request.getRequestDispatcher(TEMPLATE).forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "There has been an error!");
            request.setAttribute("username", request.getParameter("username"));
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

}
