package org.eintr.springframework.web.context.support;

import org.eintr.springframework.beans.factory.Aware;

import javax.servlet.ServletContext;

public interface ServletContextAware extends Aware {
    void setServletContext(ServletContext servletContext);
}
