package handlers.files;

import config.UrlPaths;
import database.classes.EncryptionType;
import database.classes.FileData;
import domain.utils.FileUtils;
import domain.utils.MultiPartUtils;
import domain.utils.UrlUtils;
import domain.utils.UserUtils;
import services.auth.CookieAuthorization;
import services.files.FileServiceImpl;
import services.files.interfaces.FileService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@MultipartConfig
public class FileUploadHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/fileUpload.jsp";

    private FileService fileService = new FileServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        String username = (String) request.getSession().getAttribute("username");

        try {
            FileData fileData = getFileInfoFromRequest(request);
            String fileId = fileService.saveFile(fileData);
            saveFileFromRequestPart(request, fileData.getFileName(), UserUtils.getUserFileDirectory(username));
            fileService.addOwnerToFile(username, fileId);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "There has been a problem.");
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
            return;
        }

        response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.MY_FILES_PATH);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        request.setAttribute("message", "");
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }


    private FileData getFileInfoFromRequest(HttpServletRequest request) throws Exception {
        Part filePart = request.getPart("file");
        String username = (String) request.getSession().getAttribute("username");
        String fileName = MultiPartUtils.getSubmittedFileName(filePart);
        String encryptionKey = request.getParameter("encryptionKey");
        EncryptionType encryptionType = EncryptionType.fromString(request.getParameter("encryptionType"));

        return new FileData(fileName, encryptionKey, encryptionType, UserUtils.getUserFileDirectory(username));
    }

    private void saveFileFromRequestPart(HttpServletRequest request, String fileName, String dir) throws IOException, ServletException {
        Part filePart = request.getPart("file");
        InputStream is = filePart.getInputStream();

        FileUtils.createDirectoryIfNotExists(dir);
        File file = new File(dir + File.separator + fileName);
        file.createNewFile();
        FileUtils.writeInputStreamToFile(is, file);

    }

}

