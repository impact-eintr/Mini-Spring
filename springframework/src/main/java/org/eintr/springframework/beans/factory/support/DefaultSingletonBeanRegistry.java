package org.eintr.springframework.beans.factory.support;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.DisposableBean;
import org.eintr.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
	protected static final Object NULL_OBJECT = new Object();
	private final Map<String, Object> singletonObjects = new HashMap<>();

	// BeanFactory 所有的销毁机制
	private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

	@Override
	public Object getSingleton(String beanName) {
		return singletonObjects.get(beanName);
	}

	protected void addSingleton(String beaName, Object singletonObject) {
		singletonObjects.put(beaName, singletonObject);
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
