package org.eintr.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.PropertyValue;
import org.eintr.springframework.beans.PropertyValues;
import org.eintr.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.eintr.springframework.beans.factory.config.BeanDefinition;
import org.eintr.springframework.beans.factory.config.BeanPostProcessor;
import org.eintr.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;

public abstract class AbstructAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

	protected InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();
	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Object bean;
		try {
			bean = createBeanInstance(beanDefinition, beanName, args);
			applyPropertyValues(beanName, bean, beanDefinition);
		} catch (Exception e) {
			throw new BeansException("Instantiation of bean failed", e);
		}

		// 添加这个单例
		addSingleton(beanName, bean);
		return bean;
	}

	protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
		Constructor constructorToUse = null;
		Class<?> beanClass = beanDefinition.getBeanClass();
		Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
		for (Constructor ctor : declaredConstructors)
            if (null != args && ctor.getParameterTypes().length == args.length) {
                // TODO 处理一下不同类型
                int i = 0;
                boolean flag = true;
                for (Class<?> clazz : ctor.getParameterTypes()) { // FIXME 处理自动装箱
                    if (!args[i].getClass().equals(clazz)) {
                        flag = false;
                        break;
                    }
                    i++;
                }
                if (flag) {
                    constructorToUse = ctor;
                    break;
                }
            }
		return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);
	}


	// Bean属性填充
	protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
		try {
			PropertyValues propertyValues = beanDefinition.getPropertyValues();
			for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
				String name = propertyValue.getName();
				Object value = propertyValue.getValue();

				if (value instanceof BeanReference) {
					// A 依赖 B 先获取B的实例
					BeanReference beanReference = (BeanReference) value;
					value = getBean(beanReference.getBeanName());
				}
				BeanUtil.setFieldValue(bean, name, value);
			}

		} catch (Exception e) {
			throw new BeansException("Error setting property values: "+beanName);
		}
	}

	public InstantiationStrategy getInstantiationStrategy() {
		return instantiationStrategy;
	}

	public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
		this.instantiationStrategy = instantiationStrategy;
	}



	public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
		Object result = existingBean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {

		}
		return result;
	}


	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
		Object result = existingBean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessBeforeInitialization(result, beanName);
			if ()
		}
		return result;
	}
}