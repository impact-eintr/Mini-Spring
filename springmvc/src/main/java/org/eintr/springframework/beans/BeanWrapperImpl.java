package org.eintr.springframework.beans;

import org.eintr.springframework.core.io.ResourceLoader;
import org.eintr.springframework.util.ReflectionUtils;
import org.eintr.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.NameGenerator;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static java.util.Locale.ENGLISH;

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

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return new PropertyDescriptor[0];
    }

    @Override
    public PropertyDescriptor getPropertyDescriptor(String propertyName) throws BeansException {
        try {
            if (StringUtils.hasLength(propertyName)) {
                // TODO 如何处理这里？？？？
                Class<?> clazz = this.targetBean.getClass();
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (("is"+capitalize(propertyName)).equals(method.getName())) {

                    }
                }
               // Method setermethod = clazz.getMethod(, clazz);
               // Method isermethod = this.targetBean.getClass().getMethod("is"+capitalize(propertyName));
               // Method getermethod = this.targetBean.getClass().getMethod("is"+capitalize(propertyName));
               // PropertyDescriptor pd = new PropertyDescriptor(propertyName,
               //         this.targetBean.getClass(), );
            }

            return null;
        }
        catch (IntrospectionException ex) {
            throw new BeansException("Failed to re-introspect class [" + this.targetBean.getClass().getName() + "]" + ex);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }


}
