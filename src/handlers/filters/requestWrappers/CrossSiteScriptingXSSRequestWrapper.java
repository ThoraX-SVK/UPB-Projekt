package handlers.filters.requestWrappers;

import javax.servlet.http.HttpServletRequest;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequestWrapper;

public class CrossSiteScriptingXSSRequestWrapper extends HttpServletRequestWrapper {

    private static Pattern[] patterns = new Pattern[]{
            Pattern.compile("\\s*<script>(.*?)</script>\\s*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\s*src[\r\n]*=[\r\n]*\\\'(.*?)\\\'\\s*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("\\s*src[\r\n]*=[\r\n]*\\\"(.*?)\\\"\\s*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\s*<script(.*?)>\\s*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("\\s*eval\\((.*?)\\)\\s*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("\\s*expression\\((.*?)\\)\\s*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("\\s*javascript:\\s*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\s*vbscript:\\s*", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\s*onload(.*?)\\s*=\\s*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("\\s*on(.*?)\\s*=\\s*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    public CrossSiteScriptingXSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSSAttack(values[i]);
        }

        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);

        return stripXSSAttack(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSSAttack(value);
    }

    private String stripXSSAttack(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);

            // Avoid null characters
            value = value.replaceAll("\0", "");

            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }
}