package org.eintr.springframework.beans;

import org.eintr.springframework.core.io.ResourceLoader;

import java.beans.PropertyDescriptor;
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


    /**
     * Obtain the PropertyDescriptors for the wrapped object
     * (as determined by standard JavaBeans introspection).
     * 属性描述列表
     * @return the PropertyDescriptors for the wrapped object
     */
    PropertyDescriptor[] getPropertyDescriptors();

    /**
     * Obtain the property descriptor for a specific property
     * of the wrapped object.
     * 获取属性名称对应的属性描述对象
     * @param propertyName the property to obtain the descriptor for
     * (may be a nested path, but no indexed/mapped property)
     * @return the property descriptor for the specified property
     */
    PropertyDescriptor getPropertyDescriptor(String propertyName) throws BeansException;


}
