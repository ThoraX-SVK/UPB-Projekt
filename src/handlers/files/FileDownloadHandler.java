package handlers.files;

import config.UrlPaths;
import database.classes.FileData;
import database.exceptions.DatabaseNotLoadedException;
import domain.utils.ExceptionStringUtils;
import domain.utils.FileUtils;
import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;
import services.files.FileServiceImpl;
import services.files.interfaces.FileService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileDownloadHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(FileDownloadHandler.class.getName());

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

            File file = getFile(fileId);
            if (file != null) {
                writeToResponseOutputStream(response, FileUtils.fileToByteArray(file), file.getName());
            }

        } catch (DatabaseNotLoadedException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, ExceptionStringUtils.stackTraceAsString(e));
        }
    }

    private File getFile(String fileId) {
        FileData fileData = fileService.findFile(fileId);

        File file = new File(fileData.getUploadFolder() + File.separator + fileData.getFileName());
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

