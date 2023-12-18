package org.eintr.springframework.context.event;

import org.eintr.springframework.context.ApplicationEvent;
import org.eintr.springframework.context.ApplicationListener;

// 事件广播器
public interface ApplicationEventMulticaster {
    void addApplicationListener(ApplicationListener<?> listener); // 添加监听器
    void removeapplicationListener(ApplicationListener<?> listener); // 移除监听器
    void multicastEvent(ApplicationEvent event); // 向所有的监听器广播事件
}
