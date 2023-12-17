package org.eintr.springframework.context.annotation;

import cn.hutool.core.util.StrUtil;
import org.eintr.springframework.annotation.aop.After;
import org.eintr.springframework.annotation.aop.Aspect;
import org.eintr.springframework.annotation.aop.Before;
import org.eintr.springframework.annotation.aop.PointCut;
import org.eintr.springframework.annotation.context.Scope;
import org.eintr.springframework.annotation.stereotype.Controller;
import org.eintr.springframework.annotation.stereotype.Repository;
import org.eintr.springframework.annotation.stereotype.Service;
import org.eintr.springframework.aop.MethodNode;
import org.eintr.springframework.aop.aspect.AspectMethodPointcut;
import org.eintr.springframework.aop.aspect.AspectMethodPointcutAdvisor;
import org.eintr.springframework.aop.aspect.AspectPointcutAdvisor;
import org.eintr.springframework.aop.aspect.support.DefaultAspectMethodAdvice;
import org.eintr.springframework.aop.framework.adapter.MethodAdviceInterceptor;
import org.eintr.springframework.beans.PropertyValue;
import org.eintr.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.eintr.springframework.beans.factory.config.BeanDefinition;
import org.eintr.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.eintr.springframework.annotation.stereotype.Component;
import org.eintr.springframework.util.AspectUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider { // 访问者模式
    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String  basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponenets(basePackage);
            for (BeanDefinition beanDefinition : candidates) {
                String beanScope = resolveBeanScope(beanDefinition);
                if (StrUtil.isNotEmpty(beanScope)) {
                    beanDefinition.setScope(beanScope);
                }

                if (!resolveAspect(determineBeanName(beanDefinition), beanDefinition)) {
                    registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
                }
            }
        }
        registry.registerBeanDefinition(
                StrUtil.lowerFirst(AutowiredAnnotationBeanPostProcessor.class.getName()),
                new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    private boolean resolveAspect(String beanName, BeanDefinition beanDefinition) {
        boolean aspect = false;
        Class<?> clazz = beanDefinition.getBeanClass();
        if (clazz.isAnnotationPresent(Aspect.class)) {
            for (Method method : clazz.getMethods()) {
                String annotationPath = "";

                //判断哪一个方法是切入点表达式的注解
                //like this:@PointCut("com.xichuan.dev.aop.service.StudentAopServiceImpl.study()")
                if (method.isAnnotationPresent(PointCut.class)) {
                    String delegateString = method.getAnnotation(PointCut.class).value();
                    annotationPath = delegateString;
                    //切点是方法
                    if (delegateString.charAt(delegateString.length() - 1) == ')') {
                        annotationPath = annotationPath.replace("()","");
                        String[] seg = AspectUtils.cutName(annotationPath);

                        BeanDefinition beanDefinition1 = new BeanDefinition(AspectMethodPointcutAdvisor.class);
                        beanDefinition1.getPropertyValues().addPropertyValue(new PropertyValue("aopClass", seg[0]));
                        beanDefinition1.getPropertyValues().addPropertyValue(new PropertyValue("methodName", seg[1]));
                        beanDefinition1.getPropertyValues().addPropertyValue(
                                new PropertyValue("advice",
                                        checkAspect(beanDefinition.getBeanClass(),  true, seg[1])));
                        registry.registerBeanDefinition(beanName, beanDefinition1);
                        aspect = true;
                        //切点是某个包或者类
                    }else {
                        /*
                        registry.registerBeanDefinition(beanName,
                                new BeanDefinition(AspectPointcutAdvisor.class));
                        //判断
                        URL url = BeanContainer.classLoader.getResource(basePackage.replace(".","/"));
                        //切点是class或者package,从file中加载
                        if (url.getProtocol().equals("file")){
                            addClassAndPackageAspectFromFile(clazz,annotationPath);
                        }else if (url.getProtocol().equals("jar")){   //切点是class或者package,从jar中加载
                            addClassAndPackageAspectFromJar(clazz,annotationPath);
                        }*/
                    }
                }
            }
        }
        return aspect;
    }

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (null != scope) return scope.value();
        return StrUtil.EMPTY;
    }

    private String getAnnotationValueFromBeanClass(Class<?> beanClass) {
        Component component = beanClass.getAnnotation(Component.class);
        Controller controller = beanClass.getAnnotation(Controller.class);
        Service service = beanClass.getAnnotation(Service.class);
        Repository repository = beanClass.getAnnotation(Repository.class);

        String value = StrUtil.lowerFirst(beanClass.getSimpleName());
        if (null != component && !StrUtil.isEmpty(component.value())) {
            value = component.value();
        } else if (null != controller && !StrUtil.isEmpty(controller.value())) {
            value = controller.value();
        } else if (null != service && !StrUtil.isEmpty(service.value())) {
            value = service.value();
        } else if (null != repository && !StrUtil.isEmpty(repository.value())) {
            value = repository.value();
        }
        return value;
    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        String value = getAnnotationValueFromBeanClass(beanClass);
        return value;
    }


    /**
     * 添加切入面类
     * @param clazz 切面类Class
     * @param isMethod 是否是对方法切面
     * @param MethodName 被切面的方法名字
     */
    protected Object checkAspect(Class<?> clazz, boolean isMethod, String MethodName) {
        DefaultAspectMethodAdvice defaultAspectMethodAdvice = new DefaultAspectMethodAdvice(clazz);
        for (Method method : clazz.getMethods()) {
            MethodNode methodNode=new MethodNode(method,isMethod);
            methodNode.setMethodName(MethodName);

            if(method.isAnnotationPresent(Before.class)) {
                ArrayList<MethodNode> arrayList = new ArrayList<>();
                arrayList.add(methodNode);
                defaultAspectMethodAdvice.setBeforeList(arrayList);
            }

            if(method.isAnnotationPresent(After.class)) {
                ArrayList<MethodNode> arrayList = new ArrayList<>();
                arrayList.add(methodNode);
                defaultAspectMethodAdvice.setAfterList(arrayList);
            }
        }
        return new MethodAdviceInterceptor(defaultAspectMethodAdvice);
    }
}
