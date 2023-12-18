package org.eintr.springframework.aop;

import org.aopalliance.intercept.MethodInterceptor;

public class AdvisedSupport {
    // 开启的话将使用 cglib进行代理
    private boolean proxyTargetClass = false;
    // 被代理的目标对象
    private org.eintr.springframework.aop.TargetSource targetSource;
    // 方法拦截器
    private MethodInterceptor methodInterceptor;
    // 方法匹配器(检查目标方法是否符合通知条件)
    private org.eintr.springframework.aop.MethodMatcher methodMatcher;

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public org.eintr.springframework.aop.TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(org.eintr.springframework.aop.TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public org.eintr.springframework.aop.MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public void setMethodMatcher(org.eintr.springframework.aop.MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }



}
