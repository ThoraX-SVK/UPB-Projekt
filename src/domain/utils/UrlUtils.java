package domain.utils;

import javax.servlet.http.HttpServletRequest;

public class UrlUtils {

    public static String getUrlFromRequest(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        return url.substring(0, url.length() - uri.length() + ctx.length()) + "/";
    };
}
