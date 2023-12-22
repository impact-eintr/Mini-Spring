package org.eintr.springframework.web.context.support;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.core.io.*;

import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;

public class ServletContextResourceLoader extends DefaultResourceLoader {
    private final ServletContext servletContext;

    public ServletContextResourceLoader(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Resource getResource(String location) {
        Assert.notNull(location, "Location must not be null");
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ServletContextResource(servletContext,
                    location.substring(CLASSPATH_URL_PREFIX.length()));
        } else {
            try {
                URL url = this.servletContext.getResource(location);
                return new UrlResource(url);
            } catch (MalformedURLException e) {
                return new FileSystemResource(location);
            }
        }
    }
}
