package org.eintr.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanException;
import org.eintr.springframework.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
	// 注册一个BeanDefinition
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) ;

	// 使用Beanc查询BeanDefinition
	BeanDefinition getBeanDefinition(String beanName) throws BeanException;

	// 判断是否包含指定的beanDefinition
	boolean containsBeanDefinition(String beanName);

	// 返回注册表中所有的Bean名称
	String[] getBeanDefinitionNames();
}
