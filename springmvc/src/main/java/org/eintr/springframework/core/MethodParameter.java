package org.eintr.springframework.core;

import cn.hutool.core.lang.Assert;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {

    private final Executable executable;

    private final int parameterIndex;

    private volatile Parameter parameter;


    public MethodParameter(Method method, int parameterIndex) {
        Assert.notNull(method, "Method must not be null");
        this.executable = method;
        this.parameterIndex = validateIndex(method, parameterIndex);
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

    private static int validateIndex(Executable executable, int parameterIndex) {
        int count = executable.getParameterCount();
        Assert.isTrue(parameterIndex >= -1 && parameterIndex < count);
        return parameterIndex;
    }
}
