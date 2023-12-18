package org.eintr.springframework.test.bean;

import org.eintr.springframework.annotation.beans.Autowired;

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
