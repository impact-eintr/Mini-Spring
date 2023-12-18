package org.eintr.springframework.beans.factory.config;

import org.eintr.springframework.beans.factory.HierarchicalBeanFactory;
import org.eintr.springframework.core.convert.ConversionService;
import org.eintr.springframework.util.StringValueResolver;

// 可继承 可单例
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
	String SCOPE_SINGLETON = "singleton";
	String SCOPE_PROTOTYPE = "prototype";

	// 后处理函数 专用于实例化后对象数据的处理
	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
	void destroySingletons();
	void addEmbeddedValueResolver(StringValueResolver valueResolver);
	String resolveEmbeddedValueResolver(String value);

	void setConversionService(ConversionService conversionService);

	ConversionService getConversionService();
}
