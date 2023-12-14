package org.eintr.springframework.test.bean;

import org.eintr.springframework.beans.factory.annotation.Autowired;
import org.eintr.springframework.beans.factory.annotation.Qualifier;
import org.eintr.springframework.stereotype.Component;


@Component("A")
public class A {
    @Autowired
    //@Qualifier("B")
    private B bAttr;

    public A() {
    }

    public String queryUserInfo(String name) {
        return "A";
    }

    public String register(String userName) {
        return "A";
    }
}
