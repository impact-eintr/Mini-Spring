package org.eintr.springframework.web.servlet;

import org.eintr.springframework.beans.BeanWrapper;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.PropertyAccessorFactory;
import org.eintr.springframework.beans.PropertyValues;
import org.eintr.springframework.context.ApplicationContext;
import org.eintr.springframework.context.ApplicationContextAware;
import org.eintr.springframework.util.BeanUtils;
import org.eintr.springframework.web.context.ConfigurableWebApplicationContext;
import org.eintr.springframework.web.context.WebApplicationContext;
import org.eintr.springframework.web.context.WebApplicationContextUtils;
import org.eintr.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletException;

public class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {

    public static final Class<?> DEFAULT_CONTEXT_CLASS = XmlWebApplicationContext.class;


    private Class<?> contextClass = DEFAULT_CONTEXT_CLASS;

    private WebApplicationContext webApplicationContext;

    private PropertyValues propertyValues;

    private String contextAttribute;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }


    public Class<?> getContextClass() {
        return this.contextClass;
    }

    public void setContextClass(Class<?> contextClass) {
        this.contextClass = contextClass;
    }

    public String getContextAttribute() {
        return this.contextAttribute;
    }

    protected void initServletBean(PropertyValues pvs) throws ServletException {
        try {
            this.propertyValues = pvs;
            this.webApplicationContext = initWebApplicationContext();
            initFrameworkServlet();
        } catch (ServletException | RuntimeException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext rootContext =
                WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        WebApplicationContext wac = null;

        if (this.webApplicationContext != null) {
            // TODO
        }

        if (wac == null) {
            wac = findWebApplicationContext();
        }
        if (wac == null) {
            wac = createWebApplicationContext(rootContext);
        }

        // 刷新应用上下文


        return wac;
    }


    protected WebApplicationContext findWebApplicationContext() {
        // 获取属性名称
        String attrName = getContextAttribute();
        if (attrName == null) {
            return null;
        }
        // servletContext 中寻找 attrName 的 webApplicationContext
        WebApplicationContext wac =
                WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
        if (wac == null) {
            throw new IllegalStateException("No WebApplicationContext found: initializer not registered?");
        }
        return wac;
    }

    protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent) {
        return createWebApplicationContext((ApplicationContext) parent);
    }

    protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
        Class<?> contextClass = getContextClass();
        if (null == this.propertyValues || this.propertyValues.isEmpty()) {
            return null;
        }
        String location = (String) this.propertyValues.getPropertyValues()[0].getValue();

        ConfigurableWebApplicationContext wac =
                (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
        if (parent != null) {
            wac.setParent(parent);
        }
        // 配置并刷新上下文
        configureAndRefreshWebApplicationContext(wac);
        return wac;
    }
    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {

    }

    protected void initFrameworkServlet() throws ServletException {}
}
