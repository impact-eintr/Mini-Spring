package org.eintr.springframework.beans.factory.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.BeanFactory;
import org.eintr.springframework.beans.factory.FactoryBean;
import org.eintr.springframework.beans.factory.config.BeanDefinition;
import org.eintr.springframework.beans.factory.config.BeanPostProcessor;
import org.eintr.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.eintr.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

// 可以获取一个可能实现了 FactoryBean 接口的工厂类
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
	// 所有的
	private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

	// 获取Bean
	@Override
	public Object getBean(String name) throws BeansException {
		return doGetBean(name, null);
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		return doGetBean(name, args);
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType)throws BeansException {
		return (T) getBean(name);
	}

	protected <T> T doGetBean(final String name, final Object[]args) {
		Object bean = getSingleton(name);
		if (bean != null) {
			return (T) getObjectForBeanInstance(bean, name);
		}
		BeanDefinition beanDefinition = getBeanDefinition(name);
		bean = createBean(name, beanDefinition, args); // NOTE 这里具体控制了是否使用单例模式
		return (T) getObjectForBeanInstance(bean, name) ;
	}

	private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
		if  (!(beanInstance instanceof FactoryBean)) { // 普通对象 直接构造
			return beanInstance;
		}
		// 实现了FactoryBean接口的对象
		Object object = getCachedObjectForFactoryBean(beanName);
		if (object == null) {
			FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
			object = getObjectFromFactoryBean(factoryBean, beanName); // 这里将调用FactoryBean.getObject()
		}
		return object;
	}

	protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;
	protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		this.beanPostProcessors.remove(beanPostProcessor);
		this.beanPostProcessors.add(beanPostProcessor);
	}

	public List<BeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}

	public ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}
}
