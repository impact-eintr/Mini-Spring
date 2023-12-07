package org.eintr.springframework.aop;

import java.lang.reflect.Method;

public interface MethodAfterAdvice extends AfterAdvice {
    void after(Method method, Object[] args, Object target);
}
