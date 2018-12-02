package handlers.files;

import config.UrlPaths;
import database.classes.FileData;
import database.exceptions.DatabaseNotLoadedException;
import domain.utils.FileUtils;
import domain.utils.UrlUtils;
import domain.utils.UserUtils;
import services.auth.CookieAuthorization;
import services.files.FileServiceImpl;
import services.files.interfaces.FileService;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static config.SystemFilePaths.USER_FILES_UPLOAD_DIRECTORY;

public class FileDownloadHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private FileService fileService = new FileServiceImpl();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = (String) request.getSession().getAttribute("username");
        List<String> urlParts = UrlUtils.getUrlParts(request);
        String fileId = urlParts.get(0);

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        try {
            if (!fileService.canUserAccessFile(username, fileId)) {
                response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.MY_FILES_PATH);
                return;
            }

            File file = getFile(fileId, UserUtils.getUserFileDirectory(username));
            if (file != null) {
                writeToResponseOutputStream(response, FileUtils.fileToByteArray(file), file.getName());
            }

        } catch (DatabaseNotLoadedException e) {
            e.printStackTrace();
        }
    }

    private File getFile(String fileId, String fileDir) {
        FileData fileData = fileService.findFile(fileId);

        File file = new File(fileDir + File.separator + fileData.getFileName());
        if (!file.exists()) {
            return null;
        }

        return file;
    }

    private void writeToResponseOutputStream(HttpServletResponse response, byte[] fileBytes, String fileName) throws IOException {
        response.setHeader("Content-Disposition","attachment;filename=" + fileName);
        response.setContentType("application/octet-stream");
        response.getOutputStream().write(fileBytes);
    }

}

