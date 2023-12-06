package org.eintr.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.eintr.springframework.AopProxy;
import org.eintr.springframework.aop.AdvisedSupport;

import java.lang.reflect.Method;

public class Cglib2AopProxy implements AopProxy {
    private final AdvisedSupport advised;

    public Cglib2AopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return null;
    }

}
