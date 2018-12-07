package handlers.files;

import config.UrlPaths;
import database.classes.Comment;
import database.classes.FileData;
import database.classes.UserData;
import database.exceptions.DatabaseNotLoadedException;
import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;
import services.files.CommentServiceImpl;
import services.files.FileServiceImpl;
import services.files.interfaces.CommentService;
import services.files.interfaces.FileService;
import services.user.UserServiceImpl;
import services.user.interfaces.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FileDetailHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String TEMPLATE = "templates/fileDetail.jsp";

    private FileService fileService = new FileServiceImpl();
    private CommentService commentService = new CommentServiceImpl();
    private UserService userService = new UserServiceImpl();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileId = request.getParameter("fileId");
        String username = (String) request.getSession().getAttribute("username");

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        FileData fileData = fileService.findFile(fileId);
        if (fileData == null) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.MY_FILES_PATH);
            return;
        }

        try {
            List<String> ownerUsernames = fileService.findAllFileOwnersUsernames(fileId);
            List<String> guestUsernames = fileService.findAllFileGuestsUsernames(fileId);
            List<Comment> comments = commentService.findAll(fileId);
            List<String> allUserNames = getAllUsernames();
            boolean isUserOwner = ownerUsernames.contains(username);

            request.setAttribute("comments", comments);
            request.setAttribute("owners", ownerUsernames);
            request.setAttribute("guests", guestUsernames);
            request.setAttribute("allUsers", allUserNames);
            request.setAttribute("isUserOwner", isUserOwner);

        } catch (DatabaseNotLoadedException e) {
            e.printStackTrace();
            request.setAttribute("message", "There has been a problem loading the comments on this file!");
        }

        request.setAttribute("file", fileData);
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

    private List<String> getAllUsernames() throws DatabaseNotLoadedException {
        return userService.findAll()
                .stream()
                .map(UserData::getUsername)
                .collect(Collectors.toList());
    }

}
