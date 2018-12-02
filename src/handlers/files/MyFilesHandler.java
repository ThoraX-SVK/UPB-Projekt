package handlers.files;

import config.UrlPaths;
import database.FileRepository;
import database.UserFileRelationshipRepository;
import database.classes.FileData;
import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MyFilesHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private FileRepository fileRepository = new FileRepository();
    private UserFileRelationshipRepository userFileRelationshipRepository = new UserFileRelationshipRepository();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        String username = (String) request.getSession().getAttribute("username");




        response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
    }



}
