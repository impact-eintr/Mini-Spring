package org.eintr.springframework.util;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.beans.BeansException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class BeanUtils {

    private static final Map<Class<?>, Object> DEFAULT_TYPE_VALUES;

    static {
        // 设置类型默认值
        Map<Class<?>, Object> values = new HashMap<>();
        values.put(boolean.class, false);
        values.put(byte.class, (byte) 0);
        values.put(short.class, (short) 0);
        values.put(int.class, 0);
        values.put(long.class, (long) 0);
        DEFAULT_TYPE_VALUES = Collections.unmodifiableMap(values);
    }

    public static <T> T instantiateClass(Class<T> clazz) throws BeansException {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface()) {
            throw new BeansException(clazz+"Specified class is an interface");
        }
        try {
            return instantiateClass(clazz.getDeclaredConstructor());
        } catch (NoSuchMethodException ex) {
            throw new BeansException(clazz+"No default constructor found", ex);
        } catch (LinkageError err) {
            throw new BeansException(clazz+"Unresolvable class definition", err);
        }
    }

    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeansException {
        Assert.notNull(ctor, "Constructor must not be null");
        try {
            ReflectionUtils.makeAccessible(ctor);
            Class<?>[] parameterTypes = ctor.getParameterTypes();
            Assert.isTrue(args.length <= parameterTypes.length, "Can't specify more arguments than constructor parameters");
            Object[] argsWithDefaultValues = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    Class<?> parameterType = parameterTypes[i];
                    argsWithDefaultValues[i] = (parameterType.isPrimitive() ?
                            DEFAULT_TYPE_VALUES.get(parameterType) : null);
                }
                else {
                    argsWithDefaultValues[i] = args[i];
                }
            }
            return ctor.newInstance(argsWithDefaultValues);
        } catch (InstantiationException ex) {
            throw new BeansException(ctor+"Is it an abstract class?", ex);
        } catch (IllegalAccessException ex) {
            throw new BeansException(ctor+"Is the constructor accessible?", ex);
        } catch (IllegalArgumentException ex) {
            throw new BeansException(ctor+"Illegal arguments for constructor", ex);
        } catch (InvocationTargetException ex) {
            throw new BeansException(ctor+"Constructor threw exception", ex.getTargetException());
        }
    }

}
