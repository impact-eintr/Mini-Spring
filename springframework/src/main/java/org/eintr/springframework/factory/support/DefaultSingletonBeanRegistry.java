package org.eintr.springframework.factory.support;

import org.eintr.springframework.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
	private final Map<String, Object> singletonObjects = new HashMap<>();

	@Override
	public Object getSingleton(String beanName) {
		return singletonObjects.get(beanName);
	}

	protected void addSingleton(String beaName, Object singletonObject) {
		singletonObjects.put(beaName, singletonObject);
	}
}
