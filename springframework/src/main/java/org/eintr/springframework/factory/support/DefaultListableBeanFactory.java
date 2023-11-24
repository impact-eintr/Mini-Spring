package org.eintr.springframework.factory.support;

import cn.hutool.core.bean.BeanException;
import org.eintr.springframework.BeansException;
import org.eintr.springframework.factory.ConfigureListableBeanFactory;
import org.eintr.springframework.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstructAutowireCapableBeanFactory implements BeanDefinitionRegistry{
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

	// 判断是否包含指定的beanDefinition
	public boolean containsBeanDefinition(String beanName) {
		return beanDefinitionMap.containsKey(beanName);
	}

	// 返回注册表中所有的Bean名称
	public String[] getBeanDefinitionNames() {
		return beanDefinitionMap.keySet().toArray(new String[0]);
	}

	//TODO 可配置的工厂函数的接口实现

}
