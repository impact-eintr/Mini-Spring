package org.eintr.springframework.factory.config;

import org.eintr.springframework.factory.HierarchicalBeanFactory;

// 可继承 可单例
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
	String SCOPE_SINGLETON = "singleton";
	String SCOPE_PROTOTYPE = "prototype";
}
