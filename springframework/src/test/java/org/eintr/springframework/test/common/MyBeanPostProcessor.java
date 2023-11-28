package org.eintr.springframework.test.common;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.config.BeanPostProcessor;
import org.eintr.springframework.test.bean.UserService;

public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setLocation("在前处理中修改为 天津");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
