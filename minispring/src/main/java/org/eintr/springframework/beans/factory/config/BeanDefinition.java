package org.eintr.springframework.beans.factory.config;


import org.eintr.springframework.beans.PropertyValues;

/**
 * 作者：DerekYRC https://github.com/DerekYRC/mini-spring
 */
// Bean 定义
public class BeanDefinition {

    // Bean对象的类
    private Class beanClass;

    // Bean的属性
    private PropertyValues propertyValues;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
