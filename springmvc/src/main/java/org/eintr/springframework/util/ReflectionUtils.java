package org.eintr.springframework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public abstract class ReflectionUtils {

    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }
}
