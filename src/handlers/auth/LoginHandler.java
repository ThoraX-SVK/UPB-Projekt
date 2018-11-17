package handlers.auth;

import services.auth.RegistrationService;
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

    IRegistrationService registrationService = new RegistrationService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {

            //TODO: pozriet ci je heslo spravne, ak nie je vyhodit nejaku exception, aby to nesetlo cookie
            //TODO: Teda pozriet do DB, najst usera, jeho vypocitanyHash:salt, porovnat vysledky...

            request.getRequestDispatcher(HOME).forward(request, response);

            String cookie = CookieAuthorization.createNewCookieString();
            request.getSession().setAttribute("loginCookie", cookie);
            request.getSession().setAttribute("username", username);
            CookieAuthorization.addCookie(username, cookie);
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
