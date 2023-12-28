package org.eintr.springframework.web.method;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.beans.factory.BeanFactory;
import org.eintr.springframework.util.ClassUtils;

import java.lang.reflect.Method;

public class HandlerMethod {


    private final Object bean;

    /**
     * 上下文
     */
    private final BeanFactory beanFactory;

    /**
     * bean 类型
     */
    private final Class<?> beanType;

    /**
     * 处理方法
     */
    private final Method method;


    public HandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
        Assert.notNull(beanFactory, "BeanFactory is required");
        Assert.notNull(method, "Method is required");
        this.bean = beanName;
        this.beanFactory = beanFactory;
        Class<?> beanType = beanFactory.getBean(beanName).getClass();
        if (beanType == null) {
            throw new IllegalStateException("Cannot resolve bean type for bean with name '" + beanName + "'");
        }
        this.beanType = ClassUtils.getUserClass(beanType);
        this.method = method;
    }


    public HandlerMethod(Object bean, Method method) {
        Assert.notNull(bean, "Bean is required");
        Assert.notNull(method, "Method is required");
        this.bean = bean;
        this.beanFactory = null;
        this.beanType = ClassUtils.getUserClass(bean);
        this.method = method;
    }
}
