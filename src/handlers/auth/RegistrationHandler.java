package handlers.auth;

import com.fei.upb.PasswordStrength;
import com.fei.upb.PasswordStrengthImpl;
import config.UrlPaths;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.UserAlreadyExistsException;
import domain.utils.UrlUtils;
import services.auth.RegistrationService;
import services.auth.interfaces.IRegistrationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/register.jsp";

    private String report;

    private static final Logger logger = Logger.getLogger(RegistrationHandler.class.getName());
    private IRegistrationService registrationService = new RegistrationService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("password_confirm");
        String message = null;

        logger.info("Registration attempt with username " + username);
        registrationService.isUsernameAvailable(username);

        if (!passwordsMatch(password, passwordConfirm)) {
            message = "Passwords do not match!";
        } else if (username.equals(password)) {
            message = "Password cannot be identical to username!";
        } else if (!isUsernameValid(username)) {
            message = "Username contains characters that are not letters or numbers!";
        } else if (!registrationService.isUsernameAvailable(username)) {
            message = "User with username " + username + " already exists!";
        } else if (!checkPasswordSecure(password)) {
            message = report;
        }

        if (message == null) {
            try {
                registrationService.registerUser(username, password);
                response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
                return;

            } catch (UserAlreadyExistsException e) {
                logger.warning("Registration failed with username " + username + ". " + e.getMessage());
                message = "User with username " + username + " already exists!";
            } catch (DatabaseNotLoadedException e) {
                logger.warning("Registration failed with username " + username + ". " + e.getMessage());
                message = "There has been a problem with your registration. Please try again.";
            }
        }

        logger.info("Registration attempt evaluated with message: " + message);
        request.setAttribute("message", message);
        request.setAttribute("username", username);
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

    private boolean isUsernameValid(String username) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(username);
        return !m.find();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

    private boolean passwordsMatch(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }

    private boolean checkPasswordSecure(String password) {
        PasswordStrength passwordStrength = new PasswordStrengthImpl(password);

        if (!passwordStrength.isSecure()) {
            this.report = passwordStrength.finalReport().replace(System.lineSeparator(),"<br/>");
            return false;
        } else {
            return true;
        }
    }

}
