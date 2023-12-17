package org.eintr.springframework.aop.aspect.support;


import org.eintr.springframework.aop.MethodAdvice;
import org.eintr.springframework.aop.MethodNode;

import java.lang.reflect.Method;

import java.util.List;

public class DefaultAspectMethodAdvice implements MethodAdvice {
    private List<MethodNode> beforeList;
    //切面类中，前置通知方法Map(key:被切面(需要被代理)的类; value:Method(切面类加了@After的方法;参数二:切面是否事方法；参数三:如果切面是方法，此值是方法名))
    private List<MethodNode> afterList;

    private Object target;


    public DefaultAspectMethodAdvice(Class<?> target) {
        try {
            this.target = target.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBeforeList(List<MethodNode> before) {
        this.beforeList = before;
    }

    public void setAfterList(List<MethodNode> after) {
        this.afterList = after;
    }

    @Override
    public void before(Method method, Object[] args, Object target) {
        for (MethodNode node : beforeList) {
            try {
                node.getMethod().invoke(this.target, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void after(Method method, Object[] args, Object target) {
        for (MethodNode node : afterList) {
            try {
                node.getMethod().invoke(this.target, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
