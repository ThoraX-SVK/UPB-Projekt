package handlers.crypto;

import config.UrlPaths;
import domain.crypto.symmetric.EncryptionData;
import domain.utils.ExceptionStringUtils;
import domain.utils.FileUtils;
import domain.utils.MultiPartUtils;
import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import services.encryption.EncryptionService;
import services.encryption.interfaces.IEncryptionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static config.SystemFilePaths.UPLOAD_DIRECTORY;


public class SymmetricFileDecryptionHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/SymDec.jsp";

    private static final Logger logger = Logger.getLogger(SymmetricFileDecryptionHandler.class.getName());

    private IEncryptionService encryptionService = new EncryptionService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        Date dateTime = new Date();

        if (ServletFileUpload.isMultipartContent(request)) {

            byte[] decrypted;
            try {
                decrypted = getDecryptedByteArrayFromRequest(request);

                if (decrypted != null) {
                    writeToResponseOutputStream(response, decrypted);

                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, ExceptionStringUtils.stackTraceAsString(e));
                request.setAttribute("message", dateTime.toString() + ": An error has occurred while processing your file!");
                request.getRequestDispatcher(TEMPLATE).forward(request, response);
            }

        } else {
            request.setAttribute("message", dateTime.toString() + ": Your POST request is not MultiPart content!");
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
        }

    }

    private void writeToResponseOutputStream(HttpServletResponse response, byte[] decrypted) throws IOException {
        response.setHeader("Content-Disposition","attachment;filename=" + "decryptedFile");
        response.setContentType("application/octet-stream");
        response.getOutputStream().write(decrypted);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        request.setAttribute("message", "");
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }


    private byte[] getDecryptedByteArrayFromRequest(HttpServletRequest request) throws Exception {
        List<File> files;
        files = MultiPartUtils.getFilesFromMultiParts(request, UPLOAD_DIRECTORY);

        // :(((
        File key = files.get(0);
        File content = files.get(1);

        byte[] keyBytes = FileUtils.fileToByteArray(key);
        byte[] contentBytes = FileUtils.fileToByteArray(content);

        String stringKey = new String(keyBytes);

        try {
            EncryptionData toDecrypt = EncryptionData.fromStreamAndKey(contentBytes, stringKey);
            return encryptionService.decryptData(toDecrypt);
        } catch (Exception e) {
            throw e;
        } finally {
            key.delete();
            content.delete();
        }
    }


}
