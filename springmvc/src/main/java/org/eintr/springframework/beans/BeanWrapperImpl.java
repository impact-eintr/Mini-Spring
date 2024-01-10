package org.eintr.springframework.beans;

import org.eintr.springframework.core.io.ResourceLoader;
import org.eintr.springframework.util.ReflectionUtils;
import org.eintr.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.*;
import java.util.*;

import static java.util.Locale.ENGLISH;

public class BeanWrapperImpl implements BeanWrapper {


    private Object targetBean;

    private PropertyValues pvs;

    private AccessControlContext acc;

    public BeanWrapperImpl(Object object) {
        pvs = new PropertyValues();
        targetBean = object;
    }

    @Override
    public Object getWrappedInstance() {
        return this.targetBean;
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
                Class<?> clazz = this.targetBean.getClass();
                List<Method> methods = new ArrayList<>();
                List<String> methodNames = new ArrayList<>();
                Collections.addAll(methods, clazz.getMethods());
                methods.forEach((m) -> {
                    methodNames.add(m.getName());
                });
                if (methodNames.contains("set"+capitalize(propertyName))) {

                    if (methodNames.contains("is"+capitalize(propertyName))) {
                        PropertyDescriptor pd = new PropertyDescriptor(propertyName,
                                this.targetBean.getClass(),
                                "is"+capitalize(propertyName),"set"+capitalize(propertyName) );
                        return pd;
                    } else if (methodNames.contains("get"+capitalize(propertyName))) {
                        PropertyDescriptor pd = new PropertyDescriptor(propertyName,
                                this.targetBean.getClass(),
                                "get"+capitalize(propertyName),"set"+capitalize(propertyName) );
                        pd.getWriteMethod();
                        return pd;
                    } else {
                        throw new BeansException("no getter found");
                    }
                }
                throw new BeansException("no setter found");
            }

            return null;
        }
        catch (IntrospectionException ex) {
            throw new BeansException("Failed to re-introspect class [" + this.targetBean.getClass().getName() + "]" + ex);
        }
    }


    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }



    private class BeanPropertyHandler {

        private final PropertyDescriptor pd;

        public BeanPropertyHandler(PropertyDescriptor pd) {
            this.pd = pd;
        }


        public Object getValue() throws Exception {
            // 读取函数
            final Method readMethod = this.pd.getReadMethod();
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    ReflectionUtils.makeAccessible(readMethod);
                    return null;
                });
                try {
                    // 读取函数调用
                    return AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () ->
                            readMethod.invoke(getWrappedInstance(), (Object[]) null), acc);
                }
                catch (PrivilegedActionException pae) {
                    throw pae.getException();
                }
            }
            else {
                ReflectionUtils.makeAccessible(readMethod);
                // 读取函数调用
                return readMethod.invoke(getWrappedInstance(), (Object[]) null);
            }
        }

        /**
         * 设置属性
         * @param value
         * @throws Exception
         */
        public void setValue(final Object value) throws Exception {
            final Method writeMethod = (this.pd instanceof GenericTypeAwarePropertyDescriptor ?
                    ((GenericTypeAwarePropertyDescriptor) this.pd).getWriteMethodForActualAccess() :
                    this.pd.getWriteMethod());
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    ReflectionUtils.makeAccessible(writeMethod);
                    return null;
                });
                try {
                    AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () ->
                            writeMethod.invoke(getWrappedInstance(), value), acc);
                }
                catch (PrivilegedActionException ex) {
                    throw ex.getException();
                }
            }
            else {
                ReflectionUtils.makeAccessible(writeMethod);
                writeMethod.invoke(getWrappedInstance(), value);
            }
        }
    }
}
