package org.eintr.springframework.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.DisposableBean;
import org.eintr.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;


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
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

        if (StrUtil.isNotEmpty(destroyMethodName) &&
                !(bean instanceof DisposableBean &&
                        "destroy".equals(this.destroyMethodName))) {
            Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
            if (null == destroyMethod) {
                throw new BeansException("Could't find a destroy method "+destroyMethodName);
            }
            destroyMethod.invoke(bean);
        }
    }
}
