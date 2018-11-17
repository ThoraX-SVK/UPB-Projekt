package handlers.auth;

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

    IRegistrationService registrationService = new RegistrationService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            registrationService.registerUser(username, password);
            request.getRequestDispatcher(LOGIN).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
        }

        request.setAttribute("message", "POST");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }
}
