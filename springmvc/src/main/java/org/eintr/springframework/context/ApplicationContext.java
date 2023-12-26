package org.eintr.springframework.context;

import org.eintr.springframework.beans.factory.ListableBeanFactory;
import org.eintr.springframework.beans.factory.config.AutowireCapableBeanFactory;

public interface ApplicationContext extends ListableBeanFactory, ApplicationEventPublisher {
    ApplicationContext getParent();

    AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;
}
