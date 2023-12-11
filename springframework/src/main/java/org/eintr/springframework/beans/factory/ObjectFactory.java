package org.eintr.springframework.beans.factory;

import org.eintr.springframework.beans.BeansException;

public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}
