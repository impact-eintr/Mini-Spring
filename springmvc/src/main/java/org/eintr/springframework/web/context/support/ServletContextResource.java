package org.eintr.springframework.web.context.support;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.core.io.Resource;
import org.eintr.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ServletContextResource implements Resource {

    private final ServletContext servletContext;

    private final String path;


    /**
     * Create a new ServletContextResource.
     * <p>The Servlet spec requires that resource paths start with a slash,
     * even if many containers accept paths without leading slash too.
     * Consequently, the given path will be prepended with a slash if it
     * doesn't already start with one.
     * @param servletContext the ServletContext to load from
     * @param path the path of the resource
     */
    public ServletContextResource(ServletContext servletContext, String path) {
        // check ServletContext
        Assert.notNull(servletContext, "Cannot resolve ServletContextResource without ServletContext");
        this.servletContext = servletContext;

        // check path
        Assert.notNull(path, "Path is required");
        String pathToUse = StringUtils.cleanPath(path);
        if (!pathToUse.startsWith("/")) {
            pathToUse = "/" + pathToUse;
        }
        this.path = pathToUse;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = servletContext.getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(this.path + "can not find");
        }
        return is;
    }

    @Override
    public String getFilename() {
        return path;
    }
}
