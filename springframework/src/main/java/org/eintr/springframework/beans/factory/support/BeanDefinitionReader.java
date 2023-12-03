package org.eintr.springframework.beans.factory.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.core.io.Resource;
import org.eintr.springframework.core.io.ResourceLoader;

public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegister();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeansException;
    void loadBeanDefinitions(Resource... resources) throws BeansException;
    void loadBeanDefinitions(String location) throws BeansException;
}
