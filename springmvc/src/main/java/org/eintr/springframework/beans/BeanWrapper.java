package org.eintr.springframework.beans;

import org.eintr.springframework.core.io.ResourceLoader;

import java.beans.PropertyEditor;

public interface BeanWrapper {
    /**
     * Return the bean instance wrapped by this object.
     * 获取bean实例(包装过的)
     */
    Object getWrappedInstance();

    /**
     * Return the type of the wrapped bean instance.
     * bean 包装后的类型
     */
    Class<?> getWrappedClass();

    void registerCustomEditor(Class<?> requiredType, ResourceLoader loader);

    void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown)
            throws BeansException;

    PropertyValues getPropertyValues() throws BeansException;

}
