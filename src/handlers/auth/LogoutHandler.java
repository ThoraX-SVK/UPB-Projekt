package handlers.auth;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/logout.jsp";
    private static final String HOME = "templates/homepage.jsp";

    // TODO: logout
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

    }
}
