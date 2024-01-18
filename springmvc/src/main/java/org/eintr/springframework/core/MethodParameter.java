package org.eintr.springframework.core;

import cn.hutool.core.lang.Assert;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {

    private final Executable executable;

    private final int parameterIndex;

    private volatile Parameter parameter;

    private volatile Class<?> parameterType;


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
        //if (paramType == null) {
        //    paramType = computeParameterType();
        //}
        this.parameterType = paramType;
        return paramType;
    }

    private static int validateIndex(Executable executable, int parameterIndex) {
        int count = executable.getParameterCount();
        Assert.isTrue(parameterIndex >= -1 && parameterIndex < count);
        return parameterIndex;
    }
}
