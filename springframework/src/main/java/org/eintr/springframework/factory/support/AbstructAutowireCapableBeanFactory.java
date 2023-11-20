package org.eintr.springframework.factory.support;

import org.eintr.springframework.BeansException;
import org.eintr.springframework.factory.config.BeanDefinition;

public abstract class AbstructAutowireCapableBeanFactory extends AbstractBeanFactory {
	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Object bean;
		try {
			bean = beanDefinition.getBeanClass().newInstance();
		} catch (InstantiationException |IllegalAccessException e) {
			throw new BeansException("Instantiation of bean failed", e);
		}

		// 添加这个单例
		addSingleton(beanName, bean);
		return bean;
	}
}
