package org.eintr.springframework.context.annotation;

import cn.hutool.core.util.ClassUtil;
import org.eintr.springframework.annotation.stereotype.Controller;
import org.eintr.springframework.annotation.stereotype.Repository;
import org.eintr.springframework.annotation.stereotype.Service;
import org.eintr.springframework.beans.factory.config.BeanDefinition;
import org.eintr.springframework.annotation.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClassPathScanningCandidateComponentProvider {

    private static Set<Class<? extends Annotation>> annotations = new HashSet<>();
    static {
        annotations.add(Component.class);
        annotations.add(Controller.class);
        annotations.add(Service.class);
        annotations.add(Repository.class);
    }

    public Set<BeanDefinition> findCandidateComponenets(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : annotations) {
            Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, annotation);
            if (annotation.isAnnotationPresent(Controller.class)) {
                for (Class<?> clazz : classes) {
                    candidates.add(new BeanDefinition(clazz));
                }
            } else {
                for (Class<?> clazz : classes) {
                    BeanDefinition beanDefinition = new BeanDefinition(clazz);
                    beanDefinition.setIsController(true);
                    candidates.add(beanDefinition);
                }
            }
        }
        return candidates;
    }
}
