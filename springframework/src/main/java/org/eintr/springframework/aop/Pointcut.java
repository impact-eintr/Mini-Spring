package org.eintr.springframework.aop;

// 包装切点
public interface Pointcut {
    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();
}
