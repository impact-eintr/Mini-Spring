package org.eintr.springframework.context.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.beans.Beans;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext  {
    private String[] configLocations;

    public ClassPathXmlApplicationContext() {

    }

    // 从 XML 中加载 BeanDefinition 并刷新上下文
    public ClassPathXmlApplicationContext(String configLocations) throws BeansException {
        this(new String[]{configLocations});
    }

    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException {
        this.configLocations = configLocations;
        refresh();
    }

    protected String[] getConfigLocations() {
        return configLocations;
    }
}
