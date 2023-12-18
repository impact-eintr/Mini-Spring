package org.eintr.springframework.test.common;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.config.BeanPostProcessor;
import org.eintr.springframework.annotation.stereotype.Component;
import org.eintr.springframework.test.bean.UserService;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setLocation("在类实例后处理中 天津");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
