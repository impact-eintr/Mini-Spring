package org.eintr.springframework.beans.factory.config;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.ConfigurableListableBeanFactory;

// 类信息的后处理接口 这是在实例化之前处理的
public interface BeanFactoryPostProcessor {
    // 类信息的后处理函数 这是在实例化之前处理的
    // 在所有的BeanDefinition加载完成后
    // 仍然提供一个可以修改BeanDefinition（不一定就是类本身，只是符合spring bean的部分）属性的机制
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
