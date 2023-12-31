package org.eintr.springframework.aop.framework.autoproxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.eintr.springframework.aop.*;
import org.eintr.springframework.aop.framework.ProxyFactory;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.PropertyValues;
import org.eintr.springframework.beans.factory.BeanFactory;
import org.eintr.springframework.beans.factory.BeanFactoryAware;
import org.eintr.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.eintr.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.*;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;

    private final Map<String, Object> earlyProxyReferences = Collections.synchronizedMap(new HashMap<String, Object>());

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

    // TODO spring默认实现AOP的逻辑
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return wrapIfNecessary(bean, beanName);
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(Object bean, String beanName) throws BeansException {
        return null; // FIXME 返回原值干嘛
    }

    private boolean isInfrastuctureClass(Class<?> beanClass) {
        // 判断是否是拦截器
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) ||
                Advisor.class.isAssignableFrom(beanClass);
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (isInfrastuctureClass(beanClass)) { // 如果是拦截器 不需要继续构造
            return bean;
        }
        // 对于非拦截器的bean 在beans中找到所有的拦截器
        Collection<PointcutAdvisor> advisors =
                beanFactory.getBeansOfType(PointcutAdvisor.class).values();

        for (PointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.matches(beanClass)) {
                continue;
            }

            AdvisedSupport advisedSupport = new AdvisedSupport();
            TargetSource targetSource = new TargetSource(bean);
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(false);

            Object exposedBean = new ProxyFactory(advisedSupport).getProxy();
            if (!earlyProxyReferences.containsKey(beanName)) { // 缓存
                System.out.println("============SPRING AOP 将使用用户自定义的 方法代理 构造实例========");
                earlyProxyReferences.put(beanName, exposedBean);
            }
            return exposedBean;
        }

        return bean;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        if (earlyProxyReferences.containsKey(beanName)) { //hit cache
            return earlyProxyReferences.get(beanName); // AOP代理对象
        }
        return bean; // 普通对象
    }
}
