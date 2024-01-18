package org.eintr.springframework.test.web;

import org.eintr.springframework.beans.factory.BeanFactory;
import org.eintr.springframework.core.MethodParameter;
import org.eintr.springframework.web.context.request.ServletWebRequest;
import org.eintr.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.eintr.springframework.web.servlet.mvc.method.annotation.AbstractNamedValueMethodArgumentResolver;

public class DefaultArgumentHandler extends AbstractNamedValueMethodArgumentResolver {


    public DefaultArgumentHandler(BeanFactory beanFactory) {

    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ServletWebRequest webRequest) throws Exception {
        return null;
    }
}
