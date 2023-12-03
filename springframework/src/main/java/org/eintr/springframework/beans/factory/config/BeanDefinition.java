package org.eintr.springframework.beans.factory.config;

import org.eintr.springframework.beans.PropertyValues;

public class BeanDefinition {
	private Class beanClass;
	private PropertyValues propertyValues;

	private String initMethodName;


	private String destroyMethodName;

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

	public String getDestroyMethodName() {
		return destroyMethodName;
	}

	public String getInitMethodName() {
		return initMethodName;
	}

	public void setDestroyMethodName(String destroyMethodName) {
		this.destroyMethodName = destroyMethodName;
	}

	public void setInitMethodName(String initMethodName) {
		this.initMethodName = initMethodName;
	}
}
