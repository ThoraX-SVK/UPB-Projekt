package handlers.auth;

import config.UrlPaths;
import domain.utils.ExceptionStringUtils;
import domain.utils.UrlUtils;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/login.jsp";
    private static final Logger logger = Logger.getLogger(LoginHandler.class.getName());


    private IAuthenticationService authenticationService = new AuthenticationService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        delay();

        try {
            logger.info("New login attempt. Username: " + request.getParameter("username"));

            boolean isAuthenticated = authenticationService.authenticateFromRequest(request);

            if (isAuthenticated) {
                logger.info("Login successful.");
                response.setHeader("X-XSS-Protection","1");
                response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.HOME_PATH);
            } else {
                logger.warning("Unsuccessful login");
                request.setAttribute("message", "Incorrect credentials!");
                request.setAttribute("username", request.getParameter("username"));
                request.getRequestDispatcher(TEMPLATE).forward(request, response);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, ExceptionStringUtils.stackTraceAsString(e));
            request.setAttribute("message", "There has been an error!");
            request.setAttribute("username", request.getParameter("username"));
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
        }
    }

    private void delay() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
           //do nothing
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

}
