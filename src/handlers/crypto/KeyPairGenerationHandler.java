package handlers.crypto;

import domain.utils.FileUtils;
import services.auth.CookieAuthorization;
import services.encryption.KeyGenerationServiceImpl;
import services.encryption.interfaces.KeyGenerationService;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

import static config.SystemFilePaths.ZIP_DIRECTORY;

public class KeyPairGenerationHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEMPLATE = "templates/AsymKeyPair.jsp";
    private static final String LOGIN = "templates/register.jsp";

    KeyGenerationService keyGenerationService = new KeyGenerationServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(CookieAuthorization.isNotLoggedIn(request)) {
            request.getRequestDispatcher(LOGIN).forward(request, response);
            return;
        }

        try {
            KeyPair keys = keyGenerationService.generateKeys();
            writeToResponseOutputStream(response, keys);

        } catch (NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            request.setAttribute("message", "There has been a problem.");
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(CookieAuthorization.isNotLoggedIn(request)) {
            request.getRequestDispatcher(LOGIN).forward(request, response);
            return;
        }

        request.setAttribute("message", "");
        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

    private void writeToResponseOutputStream(HttpServletResponse response, KeyPair keyPair) throws IOException {

        response.setHeader("Content-Disposition","attachment;filename=" + "keys.zip");
        response.setContentType("application/octet-stream");

        String pathname = ZIP_DIRECTORY + File.separator + "keys.zip";
        File zipFile = new File(pathname);

        Map<String, byte[]> dataMap = new HashMap<>();
        dataMap.put("public.key", keyPair.getPublic().getEncoded());
        dataMap.put("private.key", keyPair.getPrivate().getEncoded());

        byte[] zipFileByteArray = FileUtils.constructZipFileByteArray(zipFile, dataMap);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(zipFileByteArray);
        outputStream.close();
        zipFile.delete();
    }

}

