package org.eintr.springframework.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public abstract class AnnotatedElementUtils {


    static String[] PLAIN = new String[2];
    static {
        PLAIN[0] = "java.lang";
        PLAIN[1] = "org.eintr.springframework.lang";
    }

    public static boolean hasAnnotation(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        if (element.isAnnotationPresent(annotationType)) {
            return true;
        }

        for (Annotation annotation : element.getAnnotations()) {
            Class<? extends Annotation> clazz = annotation.annotationType();
            if (hasAnnotation(clazz, annotationType)) {
                return true;
            }
        }

        return false;
    }

    public static <A extends Annotation> A getAnnotation(AnnotatedElement element, Class<A> annotationType) {
        A a = (A)getAnnotationInternal(element, annotationType);

        Class<?> clazz = element.getAnnotation(annotationType).annotationType();
        Method[] methods = clazz.getDeclaredMethods();
        return a;
    }


    public static <A extends Annotation> A getAnnotationInternal(AnnotatedElement element, Class<A> annotationType) {
        if (element.isAnnotationPresent(annotationType)) {
            return (A) element.getAnnotation(annotationType);
        }

        for (Annotation annotation : element.getAnnotations()) {
            Class<? extends Annotation> clazz = annotation.annotationType();
            if (hasAnnotation(clazz, annotationType)) {
                return getAnnotationInternal(clazz, annotationType);
            }
        }

        return null;
    }


}
