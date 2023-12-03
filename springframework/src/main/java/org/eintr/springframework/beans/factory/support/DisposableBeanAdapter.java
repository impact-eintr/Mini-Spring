package org.eintr.springframework.beans.factory.support;

import org.eintr.springframework.beans.factory.DisposableBean;
import org.eintr.springframework.beans.factory.config.BeanDefinition;


public class DisposableBeanAdapter implements DisposableBean  {
    private final Object bean;
    private final String beanName;
    private String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }
    @Override
    public void destroy() throws Exception {
            // TODO
    }
}
