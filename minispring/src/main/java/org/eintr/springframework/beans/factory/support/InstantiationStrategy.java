package org.eintr.springframework.beans.factory.support;


import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 作者：DerekYRC https://github.com/DerekYRC/mini-spring
 * <p>
 * Bean 实例化策略
 */
public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args)
            throws BeansException;

}
