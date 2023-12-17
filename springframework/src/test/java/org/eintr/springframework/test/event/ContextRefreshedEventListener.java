package org.eintr.springframework.test.event;

import org.eintr.springframework.context.ApplicationListener;
import org.eintr.springframework.context.event.ContextRefreshedEvent;
import org.eintr.springframework.annotation.stereotype.Component;

@Component
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("事件刷新");
    }
}
