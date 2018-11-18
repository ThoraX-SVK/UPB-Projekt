package handlers.auth;

import com.fei.upb.PasswordStrength;
import com.fei.upb.PasswordStrengthImpl;
import config.SystemFilePaths;
import database.exceptions.UserAlreadyExistsException;
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
    private static final String LOGIN = "templates/login.jsp";

    private IRegistrationService registrationService = new RegistrationService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // TODO: check if credentials not null & password strength
        PasswordStrength passwordStrength = new PasswordStrengthImpl(password);
        try {
            registrationService.userExists(username);

            if (!passwordStrength.isSecure()) {
                throw new SecurityException("Password is weak");
            }
            registrationService.registerUser(username, password);
            request.getRequestDispatcher(LOGIN).forward(request, response);
            return;

        } catch (UserAlreadyExistsException e) {
            request.setAttribute("message", "User with username " + username + " already exists!");

        } catch (SecurityException e) {
            request.setAttribute("message", passwordStrength.finalReport().replace(System.lineSeparator(),"<br/>" ));

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "There has been an error registering your account!");
        }

        request.getRequestDispatcher(TEMPLATE).forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }
}
