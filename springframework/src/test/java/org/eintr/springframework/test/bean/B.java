package org.eintr.springframework.test.bean;

import org.eintr.springframework.beans.factory.annotation.Autowired;
import org.eintr.springframework.beans.factory.annotation.Qualifier;
import org.eintr.springframework.stereotype.Component;

public class B {
    @Autowired
    //@Qualifier("A")
    private A aAttr;

    private IUserService userService;

    public B() {
    }

    public String queryUserInfo(String name) {
        return "B";
    }

    public String register(String userName) {
        return "B";
    }
}
