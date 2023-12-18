package org.eintr.springframework.context;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
