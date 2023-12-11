package org.eintr.springframework.test.bean;

import org.eintr.springframework.beans.factory.annotation.Value;
import org.eintr.springframework.context.annotation.Scope;
import org.eintr.springframework.stereotype.Component;

@Component("A")
public class A {
    public B b;

    public A() {
        this.b = new B();
    }
}
