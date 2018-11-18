package handlers.crypto;

import config.UrlPaths;
import domain.utils.FileUtils;
import domain.utils.UrlUtils;
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

    KeyGenerationService keyGenerationService = new KeyGenerationServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
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

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
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

