package org.eintr.springframework.beans;

import org.eintr.springframework.beans.factory.BeanFactory;
import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;

public final class PropertyAccessorFactory {

    private DefaultListableBeanFactory beanFactory;

    private PropertyAccessorFactory() {
        beanFactory = new DefaultListableBeanFactory();
    }


    public static BeanWrapper forBeanPropertyAccess(Object target) {
        return new BeanWrapperImpl(target);
    }
}
