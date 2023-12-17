package org.eintr.springframework.aop.aspect;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.eintr.springframework.annotation.aop.After;
import org.eintr.springframework.annotation.aop.Before;
import org.eintr.springframework.aop.ClassFilter;
import org.eintr.springframework.aop.MethodMatcher;
import org.eintr.springframework.aop.MethodNode;
import org.eintr.springframework.aop.Pointcut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AspectMethodPointcut extends AspectPointcut implements Pointcut, ClassFilter, MethodMatcher {
    private HashSet<Class<?>> classSet = new HashSet<>();
    private HashSet<String> methodSet = new HashSet<>();
    public AspectMethodPointcut(Class<?> clazz, String MethodName) {
        classSet.add(clazz); //拦截这个类
        for (Method method : clazz.getMethods()) {
            if (MethodName.equals(method.getName())) {
                methodSet.add(MethodName); // 拦截这些方法
            }
        }
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return classSet.contains(clazz);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return methodSet.contains(method.getName());
    }
}
