package org.eintr.springframework.aop;

public interface PointcutAdvisor extends Advisor {
    Pointcut getPointcut();
}
