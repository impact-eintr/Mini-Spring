package org.eintr.springframework.beans;

import org.eintr.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.eintr.springframework.context.support.ClassPathXmlApplicationContext;
import org.eintr.springframework.core.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanWrapperImpl implements BeanWrapper {


    private Object targetBean;

    private PropertyValues pvs;

    public BeanWrapperImpl(Object object) {
        pvs = new PropertyValues();
        targetBean = object;
    }

    @Override
    public Object getWrappedInstance() {
        return null;
    }

    @Override
    public Class<?> getWrappedClass() {
        return targetBean.getClass();
    }

    @Override
    public void registerCustomEditor(Class<?> requiredType, ResourceLoader loader) {
    }

    @Override
    public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown) throws BeansException {
        this.pvs = pvs;
    }

    @Override
    public PropertyValues getPropertyValues() throws BeansException {
        return pvs;
    }

}
