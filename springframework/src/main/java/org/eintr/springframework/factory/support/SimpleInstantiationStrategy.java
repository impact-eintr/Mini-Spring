package org.eintr.springframework.factory.support;

import org.eintr.springframework.BeansException;
import org.eintr.springframework.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SimpleInstantiationStrategy implements InstantiationStrategy{

	@Override
	public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException {
		Class clazz = beanDefinition.getBeanClass();
		try {
		if (null != ctor) {
			// 带参数的构造函数
			return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
		} else {
			// 无参构造函数
			return clazz.getDeclaredConstructor().newInstance();
		}
		} catch (NoSuchMethodException|InstantiationException|IllegalAccessException| InvocationTargetException e) {
			throw new BeansException("Failed to instantiate ["+clazz.getName()+"]", e);
		}

	}
}
