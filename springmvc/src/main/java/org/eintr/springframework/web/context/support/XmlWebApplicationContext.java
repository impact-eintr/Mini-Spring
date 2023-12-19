package org.eintr.springframework.web.context.support;

import org.eintr.springframework.beans.BeansException;

import javax.servlet.ServletContext;

public class XmlWebApplicationContext extends AbstractXmlWebApplicationContext {
    private String[] configLocations;

    // 从 XML 中加载 BeanDefinition 并刷新上下文
    public XmlWebApplicationContext(ServletContext servletContext, String configLocations) throws BeansException {
        this(servletContext, new String[]{configLocations});
    }

    public XmlWebApplicationContext(ServletContext servletContext, String[] configLocations) throws BeansException {
        super(servletContext);
        this.configLocations = configLocations;
        //refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }
}
