package org.eintr.springframework.factory.support;

import org.eintr.springframework.BeansException;
import org.eintr.springframework.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public interface InstantiationStrategy {
	Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException;
}
