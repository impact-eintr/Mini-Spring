package org.eintr.springframework.web.method.support;

import org.eintr.springframework.core.MethodParameter;
import org.eintr.springframework.web.context.request.ServletWebRequest;

public interface HandlerMethodArgumentResolver {
    boolean supportsParameter(MethodParameter parameter);

    Object resolveArgument(MethodParameter parameter,
                           ServletWebRequest webRequest) throws Exception;
}
