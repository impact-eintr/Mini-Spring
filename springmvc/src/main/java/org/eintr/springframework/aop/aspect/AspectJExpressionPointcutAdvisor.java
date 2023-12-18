package org.eintr.springframework.aop.aspect;

import org.aopalliance.aop.Advice;
import org.eintr.springframework.aop.Pointcut;
import org.eintr.springframework.aop.PointcutAdvisor;

// 包装 Pointcut 和 Advice
public class AspectJExpressionPointcutAdvisor extends AspectPointcutAdvisor implements PointcutAdvisor {
    // 表达式
    private String expression;
    // 具体的拦截方法

    public void setExpression(String expression) {
        this.expression = expression;
    }


    public Pointcut getPointcut() {
        if (null == pointcut) {
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }

}
