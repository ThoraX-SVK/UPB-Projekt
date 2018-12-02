package handlers.filters;

import handlers.filters.requestWrappers.CrossSiteScriptingXSSRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class CrossSiteScriptingXSSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new CrossSiteScriptingXSSRequestWrapper((HttpServletRequest) request), response);
    }

}
