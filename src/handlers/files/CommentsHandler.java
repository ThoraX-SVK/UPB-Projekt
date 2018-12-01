package handlers.files;

import config.SystemFilePaths;
import config.UrlPaths;
import database.FileCommentsRepository;
import database.FileRepository;
import database.classes.Comment;
import database.classes.EncryptionType;
import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CommentsHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private FileRepository fileRepository = new FileRepository();
    private FileCommentsRepository fileCommentsRepository = new FileCommentsRepository();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        String username = (String) request.getSession().getAttribute("username");

        // TODO implement service to do shit with repository

        File file = new File(SystemFilePaths.UPLOAD_DIRECTORY + File.separator + "test.txt");

        String fileId = fileRepository.addFile(file.getName(), "tzvbhjvgtzhjb", EncryptionType.ASYMMETRICAL);
        String title = "Title";
        String content = "BLA BLA THANK YOU KOKOT VERY COOL";
        Comment comment = Comment
                .fromTitleAndContent(title, content)
                .writtenBy(username)
                .withCurrentDate();

        fileCommentsRepository.publish(comment, fileId);

        List<Comment> comments = fileCommentsRepository.getAllByFileId(fileId);

    }

}
