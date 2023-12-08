package org.eintr.springframework.test.event;

import org.eintr.springframework.context.ApplicationListener;
import org.eintr.springframework.context.event.ContextClosedEvent;
import org.eintr.springframework.stereotype.Component;

@Component
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("事件关闭");
    }
}
