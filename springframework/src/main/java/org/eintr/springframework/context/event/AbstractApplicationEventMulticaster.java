package org.eintr.springframework.context.event;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.BeanFactory;
import org.eintr.springframework.beans.factory.BeanFactoryAware;
import org.eintr.springframework.context.ApplicationEvent;
import org.eintr.springframework.context.ApplicationListener;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {
    public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);

    }

    @Override
    public void removeapplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    protected Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener> allListeners = new LinkedList<ApplicationListener>();
        for (ApplicationListener<ApplicationEvent> listener : applicationListeners) {
            if (supportsEvent(listener, event)) allListeners.add(listener);
        }
        return allListeners;
    }

    // 判断监听器是否关注事件
    protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener,
                                    ApplicationEvent event) {
        System.out.println("不关注事件");
        return false;
    }
}
