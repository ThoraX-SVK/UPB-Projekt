package handlers.files;

import config.UrlPaths;
import database.classes.Comment;
import database.classes.FileData;
import database.exceptions.DatabaseNotLoadedException;
import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;
import services.files.CommentServiceImpl;
import services.files.FileServiceImpl;
import services.files.interfaces.CommentService;
import services.files.interfaces.FileService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FileDetailHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static String TEMPLATE = "templates/fileDetail.jsp";

    private FileService fileService = new FileServiceImpl();
    private CommentService commentService = new CommentServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        String username = (String) request.getSession().getAttribute("username");
        String fileId = UrlUtils.getUrlParts(request).get(0);

        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        String fileId = request.getParameter("fileId");

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        FileData fileData = fileService.findFile(fileId);
        if (fileData == null) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.MY_FILES_PATH);
            return;
        }

        List<Comment> comments;
        try {
            comments = commentService.findAll(fileId);
            request.setAttribute("comments", comments);

        } catch (DatabaseNotLoadedException e) {
            e.printStackTrace();
            request.setAttribute("message", "There has been a problem loading the comments on this file!");
        }

        request.setAttribute("file", fileData);
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

}
