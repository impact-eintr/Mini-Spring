package org.eintr.springframework.test.event;

import org.eintr.springframework.context.ApplicationListener;
import org.eintr.springframework.context.event.ContextClosedEvent;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("事件关闭");
    }
}
