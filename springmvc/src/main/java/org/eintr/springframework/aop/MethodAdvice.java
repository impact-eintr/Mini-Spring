package org.eintr.springframework.aop;

import org.aopalliance.aop.Advice;

import java.lang.reflect.Method;

public interface MethodAdvice extends Advice {
    void before(Method method, Object[] args, Object target);
    void after(Method method, Object[] args, Object target);
}
