package domain.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

public class UrlUtils {

    public static String getUrlFromRequest(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        return url.substring(0, url.length() - uri.length() + ctx.length()) + "/";
    }

    public static List<String> getUrlParts(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        List<String> filteredParts = new LinkedList<>();
        for (String part : pathParts) {
            if (!part.equals("")) {
                filteredParts.add(part);
            }
        }
        return filteredParts;
    }

}
