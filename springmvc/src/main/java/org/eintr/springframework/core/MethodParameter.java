package org.eintr.springframework.core;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MethodParameter {

    private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

    private final Executable executable;

    private final int parameterIndex;

    private volatile Parameter parameter;

    private volatile Class<?> parameterType;

    private volatile String parameterName;

    private volatile Annotation[] parameterAnnotations;

    public MethodParameter(Method method, int parameterIndex) {
        Assert.notNull(method, "Method must not be null");
        this.executable = method;
        this.parameterIndex = validateIndex(method, parameterIndex);
    }


    public MethodParameter(MethodParameter original) {
        Assert.notNull(original, "Original must not be null");
        this.executable = original.executable;
        this.parameterIndex = original.parameterIndex;
        this.parameter = original.parameter;
        this.parameterType = original.parameterType;
        this.parameterName = original.parameterName;
        this.parameterAnnotations = original.parameterAnnotations;
    }


    // 获取函数参数上的注解
    public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
        Annotation[] anns = getParameterAnnotations();
        for (Annotation ann : anns) {
            if (annotationType.isInstance(ann)) {
                return (A) ann;
            }
        }
        return null;
    }

    public Annotation[] getParameterAnnotations() {
        Annotation[] annotations = this.parameterAnnotations;
        if (annotations == null) {
            Annotation[][] annotationArray = this.executable.getParameterAnnotations();
            int index = this.parameterIndex;
            if (this.executable instanceof Constructor &&
                    ClassUtils.isInnerClass(this.executable.getDeclaringClass()) &
                            annotationArray.length == this.executable.getParameterCount() - 1) {
                index = this.parameterIndex - 1;
            }
            annotations = index >= 0 && index < annotationArray.length ?
                    annotationArray[index] : EMPTY_ANNOTATION_ARRAY;
            this.parameterAnnotations = annotations;
        }
        return annotations;
    }


    public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType) {
        return (getParameterAnnotation(annotationType) != null);
    }

    public Parameter getParameter() {
        if (this.parameterIndex < 0) {
            throw new IllegalStateException("Cannot retrieve Parameter descriptor for method return type");
        }
        Parameter parameter = this.parameter;
        if (parameter == null) {
            parameter = getExecutable().getParameters()[this.parameterIndex];
            this.parameter = parameter;
        }
        return parameter;
    }


    public Class<?> getDeclaringClass() {
        return this.executable.getDeclaringClass();
    }

    public Method getMethod() {
        return (this.executable instanceof Method ? (Method) this.executable : null);
    }

    public Executable getExecutable() {
        return this.executable;
    }

    public int getParameterIndex() {
        return this.parameterIndex;
    }


    void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }


    public Class<?> getParameterType() {
        Class<?> paramType = this.parameterType;
        if (paramType != null) {
            return paramType;
        }
        //if (getContainingClass() != getDeclaringClass()) {
        //    paramType = ResolvableType.forMethodParameter(this, null, 1).resolve();
        //}
        paramType = computeParameterType();
        this.parameterType = paramType;
        return paramType;
    }

    public String getParameterName() {
        if (this.parameterIndex < 0) {
            return null;
        }
        String[] parameterNames = null;
        if (this.executable instanceof Method) {
            parameterNames = Arrays.stream(this.executable.getParameters()).
                    map(Parameter::getName).toArray(String[]::new);
        } else if (this.executable instanceof Constructor) {
            parameterNames = Arrays.stream(this.executable.getParameters()).
                    map(Parameter::getName).toArray(String[]::new);
        }
        if (parameterNames != null) {
            this.parameterName = parameterNames[this.parameterIndex];
        }
        return this.parameterName;
    }

    private Class<?> computeParameterType() {
        if (this.parameterIndex < 0) {
            Method method = getMethod();
            if (method == null) {
                return void.class;
            }
            return method.getReturnType();
        }
        return this.executable.getParameterTypes()[this.parameterIndex];
    }

    private static int validateIndex(Executable executable, int parameterIndex) {
        int count = executable.getParameterCount();
        Assert.isTrue(parameterIndex >= -1 && parameterIndex < count);
        return parameterIndex;
    }
}
