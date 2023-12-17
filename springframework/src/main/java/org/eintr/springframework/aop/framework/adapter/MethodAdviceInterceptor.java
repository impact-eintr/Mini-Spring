package org.eintr.springframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eintr.springframework.aop.MethodAdvice;

public class MethodAdviceInterceptor implements MethodInterceptor {
    private MethodAdvice advice;

    public MethodAdviceInterceptor() {

    }

    public MethodAdviceInterceptor(MethodAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        this.advice.before(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        Object bean = invocation.proceed();
        this.advice.after(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return bean;
    }
}
