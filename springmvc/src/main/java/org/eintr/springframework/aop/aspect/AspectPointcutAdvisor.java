package org.eintr.springframework.aop.aspect;

import org.aopalliance.aop.Advice;
import org.eintr.springframework.annotation.aop.After;
import org.eintr.springframework.annotation.aop.Before;
import org.eintr.springframework.aop.MethodAdvice;
import org.eintr.springframework.aop.MethodNode;
import org.eintr.springframework.aop.Pointcut;
import org.eintr.springframework.aop.PointcutAdvisor;
import org.eintr.springframework.aop.aspect.support.DefaultAspectMethodAdvice;
import org.eintr.springframework.aop.framework.adapter.MethodAdviceInterceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 包装 Pointcut 和 Advice
public abstract class AspectPointcutAdvisor implements PointcutAdvisor {
    // 切面
    protected List<AspectPointcut> pointcuts = new ArrayList<>();
    protected AspectPointcut pointcut;


    //切面类中，后置通知方法Map(key:被切面(需要被代理)的类;value:Method(参数一:切面类的方法加了@Before;参数二:切面是否事方法；参数三:如果切面是方法，此值是方法名))
    private static HashMap<String, ArrayList<MethodNode>> beforeDelegatedSet = new HashMap<>();
    //切面类中，前置通知方法Map(key:被切面(需要被代理)的类; value:Method(切面类加了@After的方法;参数二:切面是否事方法；参数三:如果切面是方法，此值是方法名))
    private static HashMap<String, ArrayList<MethodNode>> afterDelegatedSet = new HashMap<>();

    // 具体的拦截方法
    //protected Advice advice = new MethodAdviceInterceptor();
    protected Advice advice;

    @Override
    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }
}