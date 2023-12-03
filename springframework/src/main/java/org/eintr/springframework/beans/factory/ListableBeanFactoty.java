package org.eintr.springframework.beans.factory;

import org.eintr.springframework.beans.BeansException;

import java.util.Map;

public interface ListableBeanFactoty extends BeanFactory {
	<T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

	// 返回注册表中所有的Bean名称
	String[] getBeanDefinitionNames();
}
