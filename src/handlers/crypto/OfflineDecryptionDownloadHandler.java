package handlers.crypto;

import domain.utils.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import static config.SystemFilePaths.OFFLINE_DECRYPTION;


public class OfflineDecryptionDownloadHandler extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        File file = new File(OFFLINE_DECRYPTION);
        byte[] bytes = FileUtils.fileToByteArray(file);

        response.setHeader("Content-Disposition","attachment;filename=" + "OfflineDecryptor5000.jar");
        response.setContentType("application/octet-stream");
        response.getOutputStream().write(bytes);
    }
}
