package org.eintr.springframework.web.context.support;

import org.eintr.springframework.core.io.DefaultResourceLoader;
import org.eintr.springframework.core.io.Resource;

import javax.servlet.ServletContext;

public class ServletContextResourceLoader extends DefaultResourceLoader {
    private final ServletContext servletContext;

    public ServletContextResourceLoader(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Resource getResource(String location) {
        return new ServletContextResource(servletContext, location);
    }
}
