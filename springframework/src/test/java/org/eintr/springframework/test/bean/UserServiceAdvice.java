package org.eintr.springframework.test.bean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eintr.springframework.aop.MethodAfterAdvice;
import org.eintr.springframework.aop.MethodBeforeAdvice;
import org.eintr.springframework.stereotype.Component;

import java.lang.reflect.Method;

public class UserServiceAdvice implements MethodBeforeAdvice, MethodAfterAdvice, MethodInterceptor {
    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("前置拦截方法"+method.getName());
    }

    @Override
    public void after(Method method, Object[] args, Object target) {
        System.out.println("后置拦截方法"+method.getName());
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            System.out.println("监控 - Begin By AOP");
            System.out.println("方法名称：" + invocation.getMethod().getName());
            System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
            System.out.println("监控 - End\r\n");
        }
    }
}
