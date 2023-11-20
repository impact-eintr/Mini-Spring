package org.eintr.springframework.factory.support;

import org.eintr.springframework.BeansException;
import org.eintr.springframework.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) ;
}
