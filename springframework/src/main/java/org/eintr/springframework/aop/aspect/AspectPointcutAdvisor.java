package org.eintr.springframework.aop.aspect;

import org.aopalliance.aop.Advice;
import org.eintr.springframework.annotation.aop.After;
import org.eintr.springframework.annotation.aop.Before;
import org.eintr.springframework.aop.MethodAdvice;
import org.eintr.springframework.aop.MethodNode;
import org.eintr.springframework.aop.Pointcut;
import org.eintr.springframework.aop.PointcutAdvisor;
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
    protected Advice advice = new MethodAdviceInterceptor();

    @Override
    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    /**
     * 添加切入面类
     * @param clazz 切面类Class
     * @param key 被切面的类
     * @param isMethod 是否是对方法切面
     * @param MethodName 被切面的方法名字
     */
    protected void addToAspects(Class<?> clazz,String key,boolean isMethod,String MethodName) {

        MethodAdvice advice1;
        for (Method method : clazz.getMethods()) {
            MethodNode methodNode=new MethodNode(method,isMethod);
            methodNode.setMethodName(MethodName);

            if(method.isAnnotationPresent(Before.class)) {
                if(!beforeDelegatedSet.containsKey(key)) {
                    beforeDelegatedSet.put(key, new ArrayList<>());
                }
                beforeDelegatedSet.get(key).add(methodNode);
            }
            if(method.isAnnotationPresent(After.class)) {
                if(!afterDelegatedSet.containsKey(key)) {
                    afterDelegatedSet.put(key, new ArrayList<>());
                }
                afterDelegatedSet.get(key).add(methodNode);
            }
        }
    }
}
