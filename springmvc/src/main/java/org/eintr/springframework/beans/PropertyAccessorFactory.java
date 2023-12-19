package org.eintr.springframework.beans;

import org.eintr.springframework.beans.factory.BeanFactory;
import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.HashMap;
import java.util.Map;

public final class PropertyAccessorFactory {

    private static Map<Class<?>, BeanWrapper> beanWrapperMap = new HashMap<>();

    private PropertyAccessorFactory() {
    }


    public static BeanWrapper forBeanPropertyAccess(Object target) {
        if (!beanWrapperMap.containsKey(target.getClass())) {
            beanWrapperMap.put(target.getClass(), new BeanWrapperImpl(target));
        }
        return beanWrapperMap.get(target.getClass());
    }

}
