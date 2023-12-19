package org.eintr.springframework.web.context;

import org.eintr.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;

public interface WebApplicationContext extends ApplicationContext {

    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";
    /**
     * Return the standard Servlet API ServletContext for this application.
     * 获取 servlet 上下文
     */
    ServletContext getServletContext();
}
