package org.eintr.springframework.factory.support;

import org.eintr.springframework.BeansException;
import org.eintr.springframework.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstructAutowireCapableBeanFactory implements BeanDefinitionRegistry {
	private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		beanDefinitionMap.put(beanName, beanDefinition);
	}

	@Override
	protected BeanDefinition getBeanDefinition(String beanName) throws BeansException {
		BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
		if (beanDefinition == null) {
			throw new BeansException("No bean named '"+beanName+"' is defined");
		}
		return beanDefinition;
	}
}
