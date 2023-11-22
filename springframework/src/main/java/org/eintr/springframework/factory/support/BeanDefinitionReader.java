package org.eintr.springframework.factory.support;

import org.eintr.springframework.BeansException;
import org.eintr.springframework.core.io.Resource;
import org.eintr.springframework.core.io.ResourceLoader;

public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegister();

    ResourceLoader getResourceLoader();

    void loadBeanDefinition(Resource resource) throws BeansException;
    void loadBeanDefinition(Resource... resources) throws BeansException;
    void loadBeanDefinition(String location) throws BeansException;
}
