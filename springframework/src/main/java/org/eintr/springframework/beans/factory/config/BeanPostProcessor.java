package org.eintr.springframework.beans.factory.config;

import org.eintr.springframework.beans.BeansException;

// Bean实例后处理函数
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
