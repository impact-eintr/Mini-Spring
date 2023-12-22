package org.eintr.springframework.beans.factory;

import org.eintr.springframework.beans.BeansException;

public class BeanInitializationException extends BeansException {
    public BeanInitializationException(String msg) {
        super(msg);
    }

    public BeanInitializationException(String msg, Error err) {
        super(msg+err.toString());
    }

    public BeanInitializationException(String msg, Exception ex) {
        super(msg+ex.toString());
    }
}
