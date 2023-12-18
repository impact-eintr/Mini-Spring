package org.eintr.springframework.context;

import java.util.EventListener;

// 事件监听器
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
    void onApplicationEvent(E event);
}
