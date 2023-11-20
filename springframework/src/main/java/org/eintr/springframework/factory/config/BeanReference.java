package org.eintr.springframework.factory.config;

import java.lang.reflect.ParameterizedType;

public class BeanReference {
	private final String beanName;

	public BeanReference(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanName() {
		return beanName;
	}
}
