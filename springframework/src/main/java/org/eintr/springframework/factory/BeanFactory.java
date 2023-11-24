package org.eintr.springframework.factory;

import org.eintr.springframework.BeansException;

public interface BeanFactory {
	Object getBean(String name) throws BeansException;
	Object getBean(String name, Object... args) throws BeansException;

	// 根据类型获取bean
	<T> T getBean(String name, Class<T> requiredType) throws BeansException;
}
