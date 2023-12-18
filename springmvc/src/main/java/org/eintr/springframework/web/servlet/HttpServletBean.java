package org.eintr.springframework.web.servlet;

import org.eintr.springframework.beans.PropertyValues;
import org.eintr.springframework.core.env.EnvironmentCapable;

import javax.servlet.http.HttpServlet;

public class HttpServletBean extends HttpServlet implements EnvironmentCapable {

    @Override
    public final void init() throws SecurityException {
        // 获取 web.xml中的配置
        System.out.println("WEB TEST");
    }
}
