package org.eintr.springframework.factory;

import org.eintr.springframework.BeansException;

public interface BeanFactory {
	Object getBean(String name) throws BeansException;
	Object getBean(String name, Object... args) throws BeansException;
}
