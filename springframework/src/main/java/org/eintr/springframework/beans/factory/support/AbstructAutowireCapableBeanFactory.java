package org.eintr.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.PropertyValue;
import org.eintr.springframework.beans.PropertyValues;
import org.eintr.springframework.beans.factory.*;
import org.eintr.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.eintr.springframework.beans.factory.config.BeanDefinition;
import org.eintr.springframework.beans.factory.config.BeanPostProcessor;
import org.eintr.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract class AbstructAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

	protected InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();
	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Object bean;
		try {
			bean = createBeanInstance(beanDefinition, beanName, args);
			applyPropertyValues(beanName, bean, beanDefinition); // 这里还是Beanfinitiong保存好的初始信息
			// 执行bean的初始化函数
			bean = initializeBean(beanName, bean, beanDefinition); // 这里已经是经过类实例后处理后的数据
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeansException("Instantiation of bean failed", e);
		}

		registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

		// 添加这个单例
		addSingleton(beanName, bean);
		return bean;
	}

	protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
		if (bean instanceof DisposableBean ||
				StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
			registerDisposableBean(beanName, new
					DisposableBeanAdapter(bean, beanName, beanDefinition));
		}

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


	// Bean属性填充 使用定义好的Beanfinition
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
			e.printStackTrace();
			throw new BeansException("Error setting property values: "+beanName);
		}
	}

	public InstantiationStrategy getInstantiationStrategy() {
		return instantiationStrategy;
	}

	public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
		this.instantiationStrategy = instantiationStrategy;
	}

	private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
		if (bean instanceof Aware) {
			if (bean instanceof BeanFactoryAware) {
				((BeanFactoryAware) bean).setBeanFactory(this);
			}
			if (bean instanceof BeanClassLoaderAware) {
				((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
			}
			if (bean instanceof BeanNameAware) {
				((BeanNameAware) bean).setBeanName(beanName);
			}
		}
		Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

		try {
			invokeInitMethods(beanName, wrappedBean, beanDefinition);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeansException("Invocation of init method of bean[" +
					beanName + "] failed", e);
		}

		wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
		return wrappedBean;
	}

	private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
		// 优先级： 实现了初始化接口 > 指定了初始化函数
		// 1. 实现接口 InitializingBean
		if (bean instanceof InitializingBean) {
			System.out.println(beanName+" 实例化调用(2): InitializingBean.afterPropertiesSet");
			((InitializingBean) bean).afterPropertiesSet();
		}

		// 2. 注解配置 init-method {判断是为了避免二次执行初始化}
		String initMethodName = beanDefinition.getInitMethodName();
		if (StrUtil.isNotEmpty(initMethodName) && !(bean instanceof InitializingBean)) {
			Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
			if (null == initMethod) {
				throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
			}
			System.out.println(beanName+" 实例化调用(2): initMethod.invoke");
			initMethod.invoke(bean);
		}

		if (!(bean instanceof InitializingBean) &&
				!(StrUtil.isNotEmpty(initMethodName) && !(bean instanceof InitializingBean))) {
			System.out.println(beanName+" 实例化调用(2): 无自定义初始化函数");
		}
	}

	public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
		System.out.println(beanName+" 实例化调用(1): processor.postProcessBeforeInitialization");
		Object result = existingBean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessBeforeInitialization(result, beanName);
			if (null == current) // 到了链表的结尾
				return result;
			result = current;
		}
		return result;
	}


	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
		System.out.println(beanName+" 实例化调用(3): processor.postProcessAfterInitialization");
		Object result = existingBean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessAfterInitialization(result, beanName);
			if (null == current)
				return result;
			result = current;
		}
		return result;
	}
}