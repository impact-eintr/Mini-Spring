package org.eintr.springframework.test.bean;

import org.eintr.springframework.beans.factory.annotation.Autowired;
import org.eintr.springframework.stereotype.Component;

public class B {
    @Autowired
    private A aAttr;

    public B() {
    }
}
