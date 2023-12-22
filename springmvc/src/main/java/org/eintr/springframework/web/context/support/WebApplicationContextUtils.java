package org.eintr.springframework.web.context.support;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

public abstract class WebApplicationContextUtils {


    public static WebApplicationContext getWebApplicationContext(ServletContext sc) {
        return getWebApplicationContext(sc, WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }
    public static WebApplicationContext getWebApplicationContext(ServletContext sc, String attrName) {
        Assert.notNull(sc, "ServletContext must not be null");
        Object attr = sc.getAttribute(attrName);
        if (attr == null) {
            return null;
        }
        if (attr instanceof RuntimeException) {
            throw (RuntimeException) attr;
        }
        if (attr instanceof Error) {
            throw (Error) attr;
        }
        if (attr instanceof Exception) {
            throw new IllegalStateException((Exception) attr);
        }
        if (!(attr instanceof WebApplicationContext)) {
            throw new IllegalStateException("Context attribute is not of type WebApplicationContext: " + attr);
        }
        return (WebApplicationContext) attr;
    }


    public static void initServletPropertySources(ServletContext servletContext,
                                                  ServletConfig servletConfig) {

        String name = StandardServletEnvironment.SERVLET_CONTEXT_PROPERTY_SOURCE_NAME;
        //if (servletContext != null && sources.contains(name) &&
        //        sources.get(name) instanceof StubPropertySource) {
        //    // 把 name 替换成最终数据对象
        //    sources.replace(name, new ServletContextPropertySource(name, servletContext));
        //}
        String a = "";
        if (servletContext != null) {
            Enumeration<String> s = servletContext.getInitParameterNames();
            a = servletContext.getInitParameter(name);
        }
        name = StandardServletEnvironment.SERVLET_CONFIG_PROPERTY_SOURCE_NAME;
        if (servletConfig != null) {
            Enumeration<String> s = servletConfig.getInitParameterNames();
            a = servletConfig.getInitParameter(name);
        }
        //if (servletConfig != null && sources.contains(name) &&
        //        sources.get(name) instanceof StubPropertySource) {
        //    // 把 name 替换成最终数据对象
        //    sources.replace(name, new ServletConfigPropertySource(name, servletConfig));
        //}
    }
}
