package handlers.auth;

import com.fei.upb.PasswordStrength;
import com.fei.upb.PasswordStrengthImpl;
import config.UrlPaths;
import database.exceptions.UserAlreadyExistsException;
import domain.utils.UrlUtils;
import handlers.auth.exceptions.PasswordMismatchException;
import services.auth.RegistrationService;
import services.auth.interfaces.IRegistrationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrationHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/register.jsp";

    private String report;

    private IRegistrationService registrationService = new RegistrationService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("password_confirm");
        String message;

        try {
            registrationService.checkUsernameAvailable(username);
            if (!passwordsMatch(password, passwordConfirm)) {
                throw PasswordMismatchException.generic();
            }
            if (!checkPasswordSecure(password)) {
                throw new SecurityException();
            }
            registrationService.registerUser(username, password);
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;

        } catch (UserAlreadyExistsException e) {
            message = "User with username " + username + " already exists!";

        } catch (PasswordMismatchException e) {
            message = "Passwords do not match!";

        } catch (SecurityException e) {
            message = report;

        } catch (Exception e) {
            e.printStackTrace();
            message = "There has been an error registering your account!";
        }

        request.setAttribute("message", message);
        request.setAttribute("username", username);
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
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
