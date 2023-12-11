package org.eintr.springframework.beans.factory.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.DisposableBean;
import org.eintr.springframework.beans.factory.ObjectFactory;
import org.eintr.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
	protected static final Object NULL_OBJECT = new Object();
	// 一级缓存 普通对象
	private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
	// 二级缓存 提前暴露对象 没有完全实例化的对象
	private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>();
	// 三级缓存 存放代理对象
	private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>();

	// BeanFactory 所有的销毁机制
	private final Map<String, DisposableBean> disposableBeans = new LinkedHashMap<>();

	protected void addSingletonFactory(String beanName, ObjectFactory<?> factory) {
		if (!singletonObjects.containsKey(beanName)) { // 检查一级缓存
			this.singletonFactories.put(beanName, factory); // 添加至三级缓存
			this.earlySingletonObjects.remove(beanName);
		}
	}

	@Override
	public Object getSingleton(String beanName) {
		Object singletonObject = singletonObjects.get(beanName);
		if (null == singletonObject) { // 检查二级缓存
			singletonObject = earlySingletonObjects.get(beanName);
			if (null == singletonObject) { //检查三级缓存
				ObjectFactory<?> singleFactory = singletonFactories.get(beanName);
				if (null != singleFactory) {
					singletonObject = singleFactory.getObject(); // 构造对象
					earlySingletonObjects.put(beanName, singletonObject); // 将真实的对象存放进二级缓存
					singletonFactories.remove(beanName); // 移除三级缓存中的对象
				}
			}
		}
		return singletonObject;
	}

	@Override
	public void registerSingleton(String beanName, Object singletonObject) {
		singletonObjects.put(beanName, singletonObject);
	}

	public void registerDisposableBean(String beanName, DisposableBean bean) {
		disposableBeans.put(beanName, bean);
	}

	// 销毁所有的单例(有销毁机制的)
	public void destroySingletons() {
		Set<String> keySet = this.disposableBeans.keySet();
		Object[] disposableBeanNames = keySet.toArray();

		for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
			Object beanName = disposableBeanNames[i];
			DisposableBean disposableBean = disposableBeans.remove(beanName);
			try {
				disposableBean.destroy();
			} catch (Exception e) {
				throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
			}
		}
	}
}
