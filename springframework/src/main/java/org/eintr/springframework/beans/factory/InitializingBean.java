package org.eintr.springframework.beans.factory;

public interface InitializingBean {
    void afterPropertiesSet() throws Exception; // Bean 处理了属性填充后调用
}
