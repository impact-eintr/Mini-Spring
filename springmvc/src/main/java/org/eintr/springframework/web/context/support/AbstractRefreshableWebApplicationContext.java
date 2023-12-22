package org.eintr.springframework.web.context.support;

import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.eintr.springframework.context.support.AbstractRefreshableApplicationContext;
import org.eintr.springframework.web.context.ConfigurableWebApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public abstract class AbstractRefreshableWebApplicationContext extends AbstractRefreshableApplicationContext
implements ConfigurableWebApplicationContext {
    private DefaultListableBeanFactory beanFactory;
    private ServletConfig servletConfig;
    private ServletContext servletContext;


    @Override
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    public ServletConfig getServletConfig() {
        return this.servletConfig;
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

}
