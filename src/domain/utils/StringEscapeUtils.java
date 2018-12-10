package domain.utils;


public class StringEscapeUtils {


    public static String escapeHtml(String string) {
        string = string.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
        // TODO: replace all HTML special chars
        return string;
    }
}
