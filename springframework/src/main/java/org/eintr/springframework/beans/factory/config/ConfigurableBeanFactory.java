package org.eintr.springframework.beans.factory.config;

import org.eintr.springframework.beans.factory.HierarchicalBeanFactory;

// 可继承 可单例
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
	String SCOPE_SINGLETON = "singleton";
	String SCOPE_PROTOTYPE = "prototype";

	// 后处理函数 专用于实例化后对象数据的处理
	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

	void destroySingletons();
}
