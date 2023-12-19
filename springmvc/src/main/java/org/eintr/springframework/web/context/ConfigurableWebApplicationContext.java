package org.eintr.springframework.web.context;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.context.ApplicationContext;


public interface ConfigurableWebApplicationContext extends WebApplicationContext {
    // 刷新容器
    void refresh() throws BeansException;

    void registerShutdownHook();

    void setParent(ApplicationContext parent);

    void close();
}
