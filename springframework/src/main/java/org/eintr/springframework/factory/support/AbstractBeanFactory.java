package org.eintr.springframework.factory.support;

import org.eintr.springframework.BeansException;
import org.eintr.springframework.factory.BeanFactory;
import org.eintr.springframework.factory.config.BeanDefinition;

// 可以获取单例
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

	// 获取Bean
	@Override
	public Object getBean(String name) throws BeansException {
		return doGetBean(name, null);
	}

	@Override
	public Object getBean(String name, Object... args) {
		return doGetBean(name, args);
	}

	protected <T> T doGetBean(final String name, final Object[]args) {
		Object bean = getSingleton(name);
		if (bean != null) {
			return (T) bean;
		}
		BeanDefinition beanDefinition = getBeanDefinition(name);
		return (T) createBean(name, beanDefinition, args);
	}

	protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;
	protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;
}
