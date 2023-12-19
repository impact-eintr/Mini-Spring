package org.eintr.springframework.util;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.beans.BeansException;

import java.lang.reflect.Constructor;

public abstract class BeanUtils {

    public static <T> T instantiateClass(Class<T> clazz) throws BeansException {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface()) {
            throw new BeansException(clazz+"Specified class is an interface");
        }
        try {
            return instantiateClass(clazz.getDeclaredConstructor());
        }
        catch (NoSuchMethodException ex) {
            Constructor<T> ctor = findPrimaryConstructor(clazz);
            if (ctor != null) {
                return instantiateClass(ctor);
            }
            throw new BeansException(clazz+"No default constructor found", ex);
        }
        catch (LinkageError err) {
            throw new BeansException(clazz+"Unresolvable class definition", err);
        }
    }

    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeansException {

    }

}
