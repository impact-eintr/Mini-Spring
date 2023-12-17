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
            Class<?> clazz = null;
            try {
                clazz = ClassUtils.getDefaultClassLoader().loadClass(aopClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            pointcut = new AspectMethodPointcut(clazz, aopClass, methodName);

            // FIXME 添加 advice
            //addToAspects(clazz, key, true, MethodName);
        }
        return pointcut;
    }
}
