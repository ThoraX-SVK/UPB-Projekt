package handlers.files;

import config.UrlPaths;
import database.classes.Comment;
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

public class CommentsHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private FileService fileService = new FileServiceImpl();
    private CommentService commentService = new CommentServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = (String) request.getSession().getAttribute("username");
        String fileId = request.getParameter("fileId");

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        try {
            if (!fileService.canUserAccessFile(username, fileId)) {
                response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.MY_FILES_PATH);
                return;
            }
        } catch (DatabaseNotLoadedException e) {
            e.printStackTrace();
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.FILE_DETAIL_PATH + "?fileId=" + fileId);
            return;
        }

        saveCommentFromRequest(request);
        response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.FILE_DETAIL_PATH + "?fileId=" + fileId);
    }

    private void saveCommentFromRequest(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        String fileId = request.getParameter("fileId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Comment comment = Comment
                .fromTitleAndContent(title, content)
                .writtenBy(username)
                .withCurrentDate();

        try {
            commentService.publish(comment, fileId);
        } catch (DatabaseNotLoadedException | IOException e) {
            e.printStackTrace();
        }
    }

}