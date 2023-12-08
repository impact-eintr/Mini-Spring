package org.eintr.springframework.test.event;

import org.eintr.springframework.context.ApplicationListener;
import org.eintr.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("收到：" + event.getSource() + "消息;时间：" + new Date());
        System.out.println("消息：" + event.getId() + ":" + event.getMessage());
    }
}
