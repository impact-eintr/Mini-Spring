package org.eintr.springframework.web.context.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.eintr.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.eintr.springframework.core.io.Resource;

import javax.servlet.ServletContext;
import java.util.Arrays;

public class XmlWebApplicationContext extends AbstractRefreshableWebApplicationContext {


    /** Default config location for the root context. */
    public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";

    /** Default prefix for building a config location for a namespace. */
    public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";

    /** Default suffix for building a config location for a namespace. */
    public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

    private String[] configLocations;

    // 从 XML 中加载 BeanDefinition 并刷新上下文
    public XmlWebApplicationContext() throws BeansException {
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


    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public String[] getConfigLocations() {
        return configLocations;
    }

    @Override
    public void setConfigLocation(String configLocation) {
        this.configLocations = new String[]{configLocation};
    }

    @Override
    public void setConfigLocations(String... configLocations) {
        this.configLocations = (String[]) Arrays.stream(configLocations).toArray();
    }

    @Override
    public Resource getResource(String location) {
        return new ServletContextResourceLoader(this.getServletContext()).getResource(location);
    }
}
