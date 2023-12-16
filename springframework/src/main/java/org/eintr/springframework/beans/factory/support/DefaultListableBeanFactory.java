package org.eintr.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanException;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.eintr.springframework.beans.factory.config.BeanDefinition;
import org.eintr.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstructAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {
	private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

	// 注册一个BeanDefinition
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		beanDefinitionMap.put(beanName, beanDefinition);
	}

	// 使用Beanc查询BeanDefinition
	public BeanDefinition getBeanDefinition(String beanName) throws BeanException {
		BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
		if (beanDefinition == null) {
			throw new BeansException("No bean named '"+beanName+"' is defined");
		}
		return beanDefinition;
	}

	@Override
	protected boolean containsBeanDefination(String name) {
		return beanDefinitionMap.containsKey(name);
	}

	@Override
	public void preInstantiateSingletons() throws BeansException {
		for (String beanName : beanDefinitionMap.keySet()) {
			BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
			if (beanDefinition.isSingleton()) { // 未指定单例的不初始化
				getBean(beanName);
			}
		}
	}

	// 判断是否包含指定的beanDefinition
	public boolean containsBeanDefinition(String beanName) {
		return beanDefinitionMap.containsKey(beanName);
	}

	// 返回注册表中所有的Bean名称
	public String[] getBeanDefinitionNames() {
		return beanDefinitionMap.keySet().toArray(new String[0]);
	}

	@Override
	public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
		Map<String, T> result = new HashMap<>();
		beanDefinitionMap.forEach((beanName, beanDefinition)->{
			Class beanClass = beanDefinition.getBeanClass();
			if (type.isAssignableFrom(beanClass)) {
				result.put(beanName, (T)getBean(beanName));
			}
		});
		return result;
	}


	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		List<String> beanNames = new ArrayList<>();
		for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
			Class beanClass = entry.getValue().getBeanClass();
			if (requiredType.isAssignableFrom(beanClass)) {
				beanNames.add(entry.getKey());
			}
		}
		if (1 == beanNames.size()) {
			return getBean(beanNames.get(0), requiredType);
		}
		throw new BeansException(requiredType + "expected single bean but found " + beanNames.size() + ": " + beanNames);
	}
}
