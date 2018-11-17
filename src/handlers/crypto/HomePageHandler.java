package handlers.crypto;

import handlers.auth.CookieAuthorization;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomePageHandler extends HttpServlet {

    private static final String LOGIN = "templates/register.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(CookieAuthorization.isNotLoggedIn(request)) {
            request.getRequestDispatcher(LOGIN).forward(request, response);
            return;
        }

        String name = request.getParameter("name");
        if (name == null) name = "[NO NAME]";
        request.setAttribute("name", name);
        request.getRequestDispatcher("/templates/homepage.jsp").forward(request, response);
    }
}
