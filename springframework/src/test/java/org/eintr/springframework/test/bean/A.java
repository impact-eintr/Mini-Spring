package org.eintr.springframework.test.bean;

import org.eintr.springframework.beans.factory.annotation.Autowired;
import org.eintr.springframework.beans.factory.annotation.Qualifier;
import org.eintr.springframework.beans.factory.annotation.Value;
import org.eintr.springframework.stereotype.Component;


public class A {
    @Autowired
    //@Qualifier("B")
    private B bAttr;

    @Value("333")
    public int Id;

    public A() {
    }

    public String queryUserInfo(String name) {
        return "A";
    }

    public String register(String userName) {
        return "A";
    }
}
