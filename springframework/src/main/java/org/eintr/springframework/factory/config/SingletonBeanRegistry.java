package org.eintr.springframework.factory.config;

public interface SingletonBeanRegistry {
	Object getSingleton(String beanName);
}
