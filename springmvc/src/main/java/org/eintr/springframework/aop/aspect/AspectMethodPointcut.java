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
import org.eintr.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AspectMethodPointcut extends AspectPointcut implements Pointcut, ClassFilter, MethodMatcher {
    private HashSet<Class<?>> classSet = new HashSet<>();
    private HashSet<String> methodSet = new HashSet<>();
    public AspectMethodPointcut(String aopClass, String MethodNames) {

        String[] segString=aopClass.split(",");
        for (String seg : segString) {
            Class<?> clazz = null;
            try {
                clazz = ClassUtils.getDefaultClassLoader().loadClass(seg);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            classSet.add(clazz); //拦截这个类
            for (Method method : clazz.getMethods()) {
                String[] segString1 = MethodNames.split(",");
                for (String seg1 : segString1) {
                    if (seg1.equals(method.getName())) {
                        methodSet.add(seg1); // 拦截这些方法
                    }
                }
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
