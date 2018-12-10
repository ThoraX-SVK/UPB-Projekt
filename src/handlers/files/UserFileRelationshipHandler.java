package handlers.files;

import config.UrlPaths;
import database.exceptions.DatabaseNotLoadedException;
import domain.utils.ExceptionStringUtils;
import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;
import services.files.FileServiceImpl;
import services.files.interfaces.FileService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserFileRelationshipHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(UserFileRelationshipHandler.class.getName());

    private FileService fileService = new FileServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = (String) request.getSession().getAttribute("username");
        String fileId = request.getParameter("fileId");
        String selectedUser = request.getParameter("selectedUser");
        String selectedType = request.getParameter("relationshipType");

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        try {
            if (!canPrivilegeBeGranted(username, fileId)) {
                response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.MY_FILES_PATH);
                return;
            }

            switch (selectedType) {
                case "OWNER":
                    fileService.addOwnerToFile(selectedUser, fileId);
                    break;
                case "GUEST":
                    fileService.addGuestToFile(selectedUser, fileId);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

        } catch (DatabaseNotLoadedException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, ExceptionStringUtils.stackTraceAsString(e));
        }

        response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.FILE_DETAIL_PATH + "?fileId=" + fileId);
    }

    private boolean canPrivilegeBeGranted(String username, String fileId) throws DatabaseNotLoadedException {
        try {
            return fileService.isUserFileOwner(username, fileId) && fileService.fileExists(fileId);
        } catch (DatabaseNotLoadedException e) {
            throw e;
        }
    }

}
