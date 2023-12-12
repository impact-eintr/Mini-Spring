package org.eintr.springframework.test.bean;

import org.eintr.springframework.beans.factory.annotation.Value;
import org.eintr.springframework.context.annotation.Scope;
import org.eintr.springframework.stereotype.Component;

@Component("B")
public class B {
    private A a;

    public B() {
    }
}
