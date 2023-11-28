package org.eintr.springframework.beans.factory.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public interface InstantiationStrategy {
	Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException;
}
