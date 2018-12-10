package handlers.files;

import config.UrlPaths;
import database.classes.FileData;
import database.exceptions.DatabaseNotLoadedException;
import domain.utils.ExceptionStringUtils;
import domain.utils.UrlUtils;
import services.auth.CookieAuthorization;
import services.files.FileServiceImpl;
import services.files.interfaces.FileService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MyFilesHandler extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(MyFilesHandler.class.getName());

    private FileService fileService = new FileServiceImpl();
    private static final String TEMPLATE = "templates/myFiles.jsp";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (CookieAuthorization.isNotLoggedIn(request)) {
            response.sendRedirect(UrlUtils.getUrlFromRequest(request) + UrlPaths.LOGIN_PATH);
            return;
        }

        String search = request.getParameter("search");
        String username = (String) request.getSession().getAttribute("username");

        try {
            List<FileData> ownFiles = fileService.findAllOwnedFilesByUsername(username);
            List<FileData> guestFiles = fileService.findAllAccessibleFilesByUsername(username);

            ownFiles = filter(ownFiles, search);
            guestFiles = filter(guestFiles, search);

            request.setAttribute("ownFiles", ownFiles);
            request.setAttribute("guestFiles", guestFiles);

        } catch (DatabaseNotLoadedException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, ExceptionStringUtils.stackTraceAsString(e));
        }

        request.getRequestDispatcher(TEMPLATE).forward(request, response);
    }

    private List<FileData> filter(List<FileData> fileDataList, String search) {
        if (search == null)
            return fileDataList;

        return fileDataList.stream()
                .filter(fileData -> (fileData.getFileName().contains(search)))
                .collect(Collectors.toList());
    }
}
