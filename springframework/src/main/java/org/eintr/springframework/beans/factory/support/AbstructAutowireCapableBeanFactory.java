package org.eintr.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.eintr.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.PropertyValue;
import org.eintr.springframework.beans.PropertyValues;
import org.eintr.springframework.beans.factory.*;
import org.eintr.springframework.beans.factory.config.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;

public abstract class AbstructAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

	protected InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();
	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Object bean;
		try {
			// 判断是否返回代理的Bean对象
			bean = resolveBeforeInstantiation(beanName, beanDefinition);
			if (bean != null) { // 如果是代理对象将不再由spring实例化 而是交由用户自定义的代理工厂实现
				return bean;
			}
			bean = createBeanInstance(beanDefinition, beanName, args);
			// 构造完对象后再次判断是否是使用了 AOP 代理的接口
			boolean continueWithPropertyPopulation =
					applyBeanPostProcessorsAfterInstantiation(beanName, bean);
			if (!continueWithPropertyPopulation) {
				return bean;
			}

			// 新增属性 读取类属性带 @Value 注解的附带的值
			applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
			// 允许BeanPostProcessor 修改属性值 这个函数是给属性修改专用的
			applyPropertyValues(beanName, bean, beanDefinition);
			// 执行bean的初始化函数 这里已经是经过类实例后处理后的数据
			bean = initializeBean(beanName, bean, beanDefinition);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeansException("Instantiation of bean failed", e);
		}

		registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

		// 添加这个单例
		if (beanDefinition.isSingleton()) { // 通过scope指定是否设置为单例
			registerSingleton(beanName, bean);
		}
		return bean;
	}


	private boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
		boolean continueWithPropertyPopulation = true;
		for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
			if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) { // AOP 处理器
				InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor =
						(InstantiationAwareBeanPostProcessor) beanPostProcessor;
				if (!instantiationAwareBeanPostProcessor.
						postProcessAfterInstantiation(bean, beanName)) {
					continueWithPropertyPopulation = false;
					break;
				}
			}
		}
		return continueWithPropertyPopulation;
	}

	protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			if (processor instanceof InstantiationAwareBeanPostProcessor) {
				// 没有在xml中配置的属性 不会有值 也不属于BeanDefinition的属性 需要添加@Value的注解
				PropertyValues propertyValues = ((InstantiationAwareBeanPostProcessor)processor)
						.postProcessPropertyValues(beanDefinition.getPropertyValues(),
								bean,  beanName);
				if (null != propertyValues) {
					for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
						beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
					}
				}
			}
		}
	}

	protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
		Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
		if (null != bean) {
			bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
		}
		return bean;
	}

	public Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			if (processor instanceof InstantiationAwareBeanPostProcessor) {
				Object result = ((InstantiationAwareBeanPostProcessor) processor).postProcessBeforeInstantiation(beanClass, beanName);
				if (null != result) {
					return result;
				}
			}
		}
		return null;
	}

	protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
		// 非 Singleton 类型的 Bean 不执行销毁方法
		if (!beanDefinition.isSingleton()) return;

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

		// 在填充完属性值后 构造对象
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

	@Override
	public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
		Object result = existingBean;
		int i = 1;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            System.out.println(beanName + " 实例化调用(1."+(i++)+"): " + processor.getClass().getSimpleName() + ".postProcessBeforeInitialization");
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (null == current) // 到了链表的结尾
                return result;
            result = current;
        }
		return result;
	}


	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
		Object result = existingBean;
		int i = 1;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            System.out.println(beanName + " 实例化调用(3."+(i++)+"): " + processor.getClass().getSimpleName() + ".postProcessAfterInitialization");
			// DefaultAdvisorAutoProxyCreator.postProcessAfterInitialization 负责实现AOP机制
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (null == current)
                return result;
            result = current;
        }
		return result;
	}
}