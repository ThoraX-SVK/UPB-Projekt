package handlers.files;

import config.SystemFilePaths;
import config.UrlPaths;
import database.FileRepository;
import database.UserFileRelationshipRepository;
import database.classes.EncryptionType;
import database.classes.FileData;
import database.classes.UserFileRelationship;
import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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

        // TODO implement service to do shit with repository

        String username = (String) request.getSession().getAttribute("username");

        File file = new File(SystemFilePaths.UPLOAD_DIRECTORY + File.separator + "test.txt");
        String fileId = fileRepository.addFile(file.getName(), "blablabla", EncryptionType.SYMMETRICAL);
        String fileId2 = fileRepository.addFile(file.getName(), "blablabla", EncryptionType.SYMMETRICAL);

        userFileRelationshipRepository.addRecord(username);
        userFileRelationshipRepository.addOwnerToFile(username, fileId);
        userFileRelationshipRepository.addGuestToFile(username, fileId2);

        UserFileRelationship userFiles = getFilesByUsername(username);
        List<FileData> ownedFiles = fileRepository.findByIds(userFiles.getOwnedFiles());
        List<FileData> accessibleFiles = fileRepository.findByIds(userFiles.getAccessibleFiles());

        response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
    }

    private UserFileRelationship getFilesByUsername(String username) throws IOException {
        UserFileRelationship ufr = userFileRelationshipRepository.findByUsername(username);
        return ufr;
    }


}
