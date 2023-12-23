package org.eintr.springframework.web.servlet.resource;

import org.eintr.springframework.web.HttpRequestHandler;
import org.eintr.springframework.web.context.support.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultServletHttpRequestHandler implements HttpRequestHandler, ServletContextAware {
    @Override
    public void setServletContext(ServletContext servletContext) {

    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
