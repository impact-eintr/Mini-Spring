package org.eintr.springframework.aop.aspect;

import org.eintr.springframework.annotation.aop.After;
import org.eintr.springframework.annotation.aop.Before;
import org.eintr.springframework.aop.ClassFilter;
import org.eintr.springframework.aop.MethodMatcher;
import org.eintr.springframework.aop.MethodNode;
import org.eintr.springframework.aop.Pointcut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AspectPointcut implements Pointcut, ClassFilter, MethodMatcher {




    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }


}
