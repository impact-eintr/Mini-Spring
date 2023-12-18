package org.eintr.springframework.beans;

import org.eintr.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.eintr.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Set;

public class BeanWrapperImpl implements BeanWrapper {


    private Class<?> targetBeanClass;

    public BeanWrapperImpl(Object object) {
        targetBeanClass = object.getClass();
    }

    @Override
    public Object getWrappedInstance() {
        return null;
    }

    @Override
    public Class<?> getWrappedClass() {
        return targetBeanClass;
    }

    @Override
    public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown) throws BeansException {

    }
}
