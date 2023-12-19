package org.eintr.springframework.web.context.support;

import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.eintr.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import javax.servlet.ServletContext;

public abstract class AbstractXmlWebApplicationContext extends AbstractRefreshableWebApplicationContext {
    public AbstractXmlWebApplicationContext(ServletContext servletContext) {
        super(servletContext);
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

    protected abstract String[] getConfigLocations();
}
