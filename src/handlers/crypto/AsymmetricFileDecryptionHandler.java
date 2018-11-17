package handlers.crypto;

import domain.crypto.asymmetric.AsymmetricDecryptionData;
import domain.crypto.symmetric.EncryptionData;
import domain.utils.FileUtils;
import domain.utils.MultiPartUtils;
import services.auth.CookieAuthorization;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import services.encryption.AsymmetricDecryptionServiceImpl;
import services.encryption.EncryptionService;
import services.encryption.interfaces.AsymmetricDecryptionService;
import services.encryption.interfaces.IEncryptionService;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static config.SystemFilePaths.UPLOAD_DIRECTORY;
import static config.SystemFilePaths.ZIP_DIRECTORY;


public class AsymmetricFileDecryptionHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/AsymDec.jsp";
    private static final String LOGIN = "templates/register.jsp";

    private AsymmetricDecryptionService asymmetricDecryptionService = new AsymmetricDecryptionServiceImpl();
    private IEncryptionService encryptionService = new EncryptionService();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(CookieAuthorization.isNotLoggedIn(request)) {
            request.getRequestDispatcher(LOGIN).forward(request, response);
            return;
        }

        Date dateTime = new Date();

        if (ServletFileUpload.isMultipartContent(request)) {

            AsymmetricDecryptionData decrypted;
            try {
                decrypted = getDecryptionDataFromRequest(request);
                writeToResponseOutputStream(response, decrypted);

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("message", dateTime.toString() + ": An error has occurred while processing your file!");
                request.getRequestDispatcher(TEMPLATE).forward(request, response);
            }

        } else {
            request.setAttribute("message", dateTime.toString() + ": Your POST request is not MultiPart content!");
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
        }

    }

    private void writeToResponseOutputStream(HttpServletResponse response, AsymmetricDecryptionData decrypted) throws IOException {
        response.setHeader("Content-Disposition","attachment;filename=" + "asym_decrypted.zip");
        response.setContentType("application/octet-stream");

        String pathname = ZIP_DIRECTORY + File.separator + "asym_decrypted.zip";
        File zipFile = new File(pathname);

        Map<String, byte[]> dataMap = new HashMap<>();
        dataMap.put("decrypted.bin", decrypted.getDecryptedByteArray());

        byte[] zipFileByteArray = FileUtils.constructZipFileByteArray(zipFile, dataMap);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(zipFileByteArray);
        outputStream.close();
        zipFile.delete();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(CookieAuthorization.isNotLoggedIn(request)) {
            request.getRequestDispatcher(LOGIN).forward(request, response);
            return;
        }

        request.setAttribute("message", "");
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

    private AsymmetricDecryptionData getDecryptionDataFromRequest(HttpServletRequest request) throws Exception {
        List<File> files;
        files = MultiPartUtils.getFilesFromMultiParts(request, UPLOAD_DIRECTORY);

        File prKey = files.get(0);
        File key = files.get(1);
        File content = files.get(2);

        byte[] prKeyBytes = FileUtils.fileToByteArray(prKey);
        byte[] keyBytes = FileUtils.fileToByteArray(key);
        byte[] contentBytes = FileUtils.fileToByteArray(content);

        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(prKeyBytes));
        byte[] decryptedKey = asymmetricDecryptionService.decrypt(keyBytes, privateKey);
        byte[] decryptedData = encryptionService.decryptData(EncryptionData.fromStreamAndKey(contentBytes, new String(decryptedKey)));

        try {
            return AsymmetricDecryptionData.fromStreamAndKey(decryptedData, privateKey);
        } catch (Exception e) {
            throw e;
        } finally {
            prKey.delete();
            key.delete();
            content.delete();
        }
    }


}
