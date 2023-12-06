package org.eintr.springframework.aop;

import java.lang.reflect.Method;

// 方法匹配用于找到表达式范围内匹配的目标类和方法 methodMatcher.matches(method, targetObj.getClass())
public interface MethodMatcher {
    boolean matches(Method method, Class<?> targetClass);
}
