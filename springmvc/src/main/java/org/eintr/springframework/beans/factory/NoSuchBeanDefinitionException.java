package org.eintr.springframework.beans.factory;

import org.eintr.springframework.beans.BeansException;

public class NoSuchBeanDefinitionException extends BeansException {

    public NoSuchBeanDefinitionException(String msg) {
        super(msg);
    }
}
