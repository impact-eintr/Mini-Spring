package org.eintr.springframework.beans;

import org.eintr.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Properties;

public class BeanPropertyHandler {

    private final BeanWrapperImpl beanWrapper;
    private final PropertyDescriptor pd;

    public BeanPropertyHandler(BeanWrapperImpl beanWrapper, PropertyDescriptor pd) {
        this.beanWrapper = beanWrapper;
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
                        readMethod.invoke(beanWrapper.getWrappedInstance(), (Object[]) null), beanWrapper.getAcc());
            } catch (PrivilegedActionException pae) {
                throw pae.getException();
            }
        } else {
            ReflectionUtils.makeAccessible(readMethod);
            // 读取函数调用
            return readMethod.invoke(beanWrapper.getWrappedInstance(), (Object[]) null);
        }
    }

    /**
     * 设置属性
     *
     * @param value
     * @throws Exception
     */
    public void setValue(final Object value) throws Exception {
        //final Method writeMethod = (this.pd instanceof GenericTypeAwarePropertyDescriptor ?
        //        ((GenericTypeAwarePropertyDescriptor) this.pd).getWriteMethodForActualAccess() :
        //        this.pd.getWriteMethod());


        final Method writeMethod = pd.getWriteMethod();
        if (System.getSecurityManager() != null) {
            AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                ReflectionUtils.makeAccessible(writeMethod);
                return null;
            });
            try {
                AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () ->
                        writeMethod.invoke(beanWrapper.getWrappedInstance(), value), beanWrapper.getAcc());
            } catch (PrivilegedActionException ex) {
                throw ex.getException();
            }
        } else {
            ReflectionUtils.makeAccessible(writeMethod);
            writeMethod.invoke(beanWrapper.getWrappedInstance(), value);
        }
    }

}
