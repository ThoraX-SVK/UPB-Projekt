package handlers.files;

import config.UrlPaths;
import database.classes.FileData;
import database.exceptions.DatabaseNotLoadedException;
import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;
import services.files.FileServiceImpl;
import services.files.interfaces.FileService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserFilePrivilegeHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static String TEMPLATE = "templates/myFiles.jsp";

    private FileService fileService = new FileServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        String username = (String) request.getSession().getAttribute("username");
        String selectedUser = (String) request.getAttribute("selectedUser");
        String selectedFileId = (String) request.getAttribute("fileId");

        try {
            fileService.addGuestToFile(selectedUser, selectedFileId);
        } catch (DatabaseNotLoadedException e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }
}
