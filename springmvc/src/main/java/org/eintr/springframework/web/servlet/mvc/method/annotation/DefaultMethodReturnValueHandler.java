package org.eintr.springframework.web.servlet.mvc.method.annotation;

import org.eintr.springframework.core.MethodParameter;
import org.eintr.springframework.web.context.request.ServletWebRequest;
import org.eintr.springframework.web.method.support.HandlerMethodReturnValueHandler;

public class DefaultMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return true;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ServletWebRequest webRequest) throws Exception {

    }
}
