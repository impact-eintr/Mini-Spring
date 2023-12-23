package org.eintr.springframework.web.context.support;

import org.eintr.springframework.context.support.ApplicationObjectSupport;

import javax.servlet.ServletContext;

public class WebApplicationObjectSupport extends ApplicationObjectSupport
        implements ServletContextAware {
    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != this.servletContext) {
            this.servletContext = servletContext;
            initServletContext(servletContext);
        }
    }

    protected void initServletContext(ServletContext servletContext) {}
}
