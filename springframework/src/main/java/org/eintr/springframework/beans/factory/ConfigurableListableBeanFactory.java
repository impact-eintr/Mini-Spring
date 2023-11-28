package org.eintr.springframework.beans.factory;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.eintr.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.eintr.springframework.beans.factory.config.BeanDefinition;

// 可列出 可自动装填 可配置 可单例 可继承 的工厂类
public interface ConfigurableListableBeanFactory extends ListableBeanFactoty, AutowireCapableBeanFactory, ConfigurableBeanFactory {
	BeanDefinition getBeanDefinition(String beanName) throws BeansException;
	void preInstantiateSingletons() throws BeansException;

}
