package handlers;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import domain.crypto.symmetric.EncryptionData;
import domain.utils.FileUtils;
import domain.utils.MultiPartUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import services.encryption.EncryptionService;
import services.encryption.interfaces.IEncryptionService;

import static config.SystemFilePaths.ZIP_DIRECTORY;
import static config.SystemFilePaths.UPLOAD_DIRECTORY;


public class SymmetricFileEncryptionHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/SymEnc.jsp";

    private IEncryptionService encryptionService = new EncryptionService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Date dateTime = new Date();

        if (ServletFileUpload.isMultipartContent(request)) {

            EncryptionData encrypted;
            try {
                encrypted = getEncryptionDataFromRequest(request);

                if (encrypted != null) {
                    writeToResponseOutputStream(response, encrypted);
                }

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

    private void writeToResponseOutputStream(HttpServletResponse response, EncryptionData encrypted) throws IOException {
        response.setHeader("Content-Disposition", "attachment;filename=" + "encrypted_file.zip");
        response.setContentType("application/octet-stream");

        String pathname = ZIP_DIRECTORY + File.separator + "encrypted.zip";
        FileUtils.createDirectoryIfNotExists(ZIP_DIRECTORY);
        File zipFile = new File(pathname);

        Map<String, byte[]> dataMap = new HashMap<>();
        dataMap.put("key.key", encrypted.getKey().getBytes());
        dataMap.put("encrypted.bin", encrypted.getEncryptedByteArray());

        byte[] zipFileByteArray = FileUtils.constructZipFileByteArray(zipFile, dataMap);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(zipFileByteArray);
        outputStream.close();
        zipFile.delete();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("message", "");
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }


    private EncryptionData getEncryptionDataFromRequest(HttpServletRequest request) throws Exception {
        File file = MultiPartUtils.getFileFromMultiParts(request, UPLOAD_DIRECTORY);

        byte[] dataToEncrypt = FileUtils.fileToByteArray(file);

        try {
            return encryptionService.encryptByteArray(dataToEncrypt);
        } catch (Exception e) {
            throw e;
        } finally {
            file.delete();
        }
    }


}
