package org.eintr.springframework.beans.factory.config;

import org.eintr.springframework.beans.PropertyValues;

public class BeanDefinition {

	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;
	private Class beanClass;
	private PropertyValues propertyValues;

	private String initMethodName;

	private String destroyMethodName;

	private String scope = SCOPE_SINGLETON;

	private boolean singleton = true;

	private boolean prototype = false;

	//是否是Controller注解
	private  boolean isController = false;

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

	public void setScope(String scope) {
		this.scope = scope;
		this.singleton = SCOPE_SINGLETON.equals(scope);
		this.prototype = SCOPE_PROTOTYPE.equals(scope);
	}

	public boolean isPrototype() {
		return prototype;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public boolean isController() {
		return isController;
	}

	public void setIsController(boolean controller) {
		this.isController = controller;
	}
}
