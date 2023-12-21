package org.eintr.springframework.web.context.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.eintr.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import javax.servlet.ServletContext;

public class XmlWebApplicationContext extends AbstractRefreshableWebApplicationContext {
    private String[] configLocations;

    // 从 XML 中加载 BeanDefinition 并刷新上下文
    public XmlWebApplicationContext(ServletContext servletContext, String configLocations) throws BeansException {
        this(servletContext, new String[]{configLocations});
    }

    public XmlWebApplicationContext(ServletContext servletContext, String[] configLocations) throws BeansException {
        //super(servletContext);
        this.configLocations = configLocations;
        //refresh();
    }


    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader =
                new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (null != configLocations) {
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    public String[] getConfigLocations() {
        return configLocations;
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    //@Override
    public ServletContext getServletContext() {
        return null;
    }
}
