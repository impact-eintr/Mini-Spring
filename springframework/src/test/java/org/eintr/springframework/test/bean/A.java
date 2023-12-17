package org.eintr.springframework.test.bean;

import org.eintr.springframework.annotation.beans.Autowired;
import org.eintr.springframework.annotation.beans.Value;


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
