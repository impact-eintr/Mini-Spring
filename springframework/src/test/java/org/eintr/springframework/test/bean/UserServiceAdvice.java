package org.eintr.springframework.test.bean;

import org.eintr.springframework.aop.MethodAfterAdvice;
import org.eintr.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class UserServiceAdvice implements MethodBeforeAdvice, MethodAfterAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("前置拦截方法"+method.getName());
    }

    @Override
    public void after(Method method, Object[] args, Object target) {
        System.out.println("后置拦截方法"+method.getName());
    }
}
