package org.eintr.springframework.aop.aspect;

import org.eintr.springframework.aop.Pointcut;
import org.eintr.springframework.aop.PointcutAdvisor;
import org.eintr.springframework.util.ClassUtils;

// 包装 Pointcut 和 Advice
public class AspectMethodPointcutAdvisor extends AspectPointcutAdvisor implements PointcutAdvisor {
        private String aopClass;
        private String methodName;

    public Pointcut getPointcut() {
        if (null == pointcut) {
            pointcut = new AspectMethodPointcut(aopClass, methodName);
        }
        return pointcut;
    }
}
