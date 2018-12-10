package handlers.crypto;

import config.UrlPaths;
import domain.crypto.asymmetric.AsymmetricEncryptionData;
import domain.crypto.symmetric.EncryptionData;
import domain.utils.ExceptionStringUtils;
import domain.utils.FileUtils;
import domain.utils.MultiPartUtils;
import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import services.encryption.AsymmetricEncryptionServiceImpl;
import services.encryption.EncryptionService;
import services.encryption.interfaces.AsymmetricEncryptionService;
import services.encryption.interfaces.IEncryptionService;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static config.SystemFilePaths.UPLOAD_DIRECTORY;
import static config.SystemFilePaths.ZIP_DIRECTORY;


public class AsymmetricFileEncryptionHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/AsymEnc.jsp";

    private static final Logger logger = Logger.getLogger(AsymmetricFileDecryptionHandler.class.getName());


    private AsymmetricEncryptionService asymmetricEncryptionService = new AsymmetricEncryptionServiceImpl();
    private IEncryptionService encryptionService = new EncryptionService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        Date dateTime = new Date();

        if (ServletFileUpload.isMultipartContent(request)) {

            AsymmetricEncryptionData encrypted;
            try {
                encrypted = getEncryptionDataFromRequest(request);
                writeToResponseOutputStream(response, encrypted);

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

    private void writeToResponseOutputStream(HttpServletResponse response, AsymmetricEncryptionData encrypted) throws IOException {

        response.setHeader("Content-Disposition","attachment;filename=" + "asym_encrypted.zip");
        response.setHeader("X-XSS-Protection","1");
        response.setContentType("application/octet-stream");

        String pathname = ZIP_DIRECTORY + File.separator + "asym_encrypted.zip";
        File zipFile = new File(pathname);

        Map<String, byte[]> dataMap = new HashMap<>();
        dataMap.put("encrypted_key.key", encrypted.getKey());
        dataMap.put("encrypted.bin", encrypted.getEncryptedByteArray());

        byte[] zipFileByteArray = FileUtils.constructZipFileByteArray(zipFile, dataMap);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(zipFileByteArray);
        outputStream.close();
        zipFile.delete();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        request.setAttribute("message", "");
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }


    private AsymmetricEncryptionData getEncryptionDataFromRequest(HttpServletRequest request) throws Exception {
        List<File> files;
        files = MultiPartUtils.getFilesFromMultiParts(request, UPLOAD_DIRECTORY);

        File pubKey = files.get(0);
        File content = files.get(1);

        byte[] pubKeyBytes = FileUtils.fileToByteArray(pubKey);
        byte[] contentBytes = FileUtils.fileToByteArray(content);

        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKeyBytes));
        EncryptionData encryptedData = encryptionService.encryptByteArray(contentBytes);
        byte[] encryptedKey = asymmetricEncryptionService.encrypt(encryptedData.getKey().getBytes(), publicKey);

        try {
            return AsymmetricEncryptionData.fromStreamAndKeys(
                    encryptedData.getEncryptedByteArray(),
                    encryptedKey,
                    publicKey);
        } catch (Exception e) {
            throw e;
        } finally {
            pubKey.delete();
            content.delete();
        }
    }

}
