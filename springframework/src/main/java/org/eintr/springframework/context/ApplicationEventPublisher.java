package org.eintr.springframework.context;

public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
