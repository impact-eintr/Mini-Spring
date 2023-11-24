package org.eintr.springframework.factory;

import org.eintr.springframework.BeansException;
import org.eintr.springframework.factory.config.AutowireCapableBeanFactory;
import org.eintr.springframework.factory.config.BeanDefinition;
import org.eintr.springframework.factory.config.ConfigurableBeanFactory;

// 可列出 可自动装填 可配置 可单例 可继承 的工厂类
public interface ConfigureListableBeanFactory extends ListableBeanFactoty, AutowireCapableBeanFactory, ConfigurableBeanFactory {
	BeanDefinition getDefinition(String beanName) throws BeansException;
}
