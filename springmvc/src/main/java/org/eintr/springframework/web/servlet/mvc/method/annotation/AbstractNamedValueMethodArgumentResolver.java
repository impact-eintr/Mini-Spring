package org.eintr.springframework.web.servlet.mvc.method.annotation;

import org.eintr.springframework.core.MethodParameter;
import org.eintr.springframework.web.context.request.ServletWebRequest;
import org.eintr.springframework.web.method.support.HandlerMethodArgumentResolver;

public abstract class AbstractNamedValueMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ServletWebRequest webRequest) throws Exception {
        return null;
    }
}
