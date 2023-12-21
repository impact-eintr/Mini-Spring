package org.eintr.springframework.web.context.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.eintr.springframework.context.support.AbstractRefreshableApplicationContext;
import org.eintr.springframework.web.context.ConfigurableWebApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public  abstract class AbstractRefreshableWebApplicationContext extends AbstractRefreshableApplicationContext
implements ConfigurableWebApplicationContext {
    private DefaultListableBeanFactory beanFactory;
    private ServletConfig servletConfig;
    private ServletContext servletContext;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
