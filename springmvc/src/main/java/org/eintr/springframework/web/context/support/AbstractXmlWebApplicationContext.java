package org.eintr.springframework.web.context.support;

import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.eintr.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.eintr.springframework.context.support.AbstractRefreshableApplicationContext;

import javax.servlet.ServletContext;

public abstract class AbstractXmlWebApplicationContext extends AbstractRefreshaleApplicationContext {


    protected abstract String[] getConfigLocations();
}
