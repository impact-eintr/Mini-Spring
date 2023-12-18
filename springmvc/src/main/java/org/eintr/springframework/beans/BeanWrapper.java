package org.eintr.springframework.beans;

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


    void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown)
            throws BeansException;

}
