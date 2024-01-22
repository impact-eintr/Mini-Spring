package org.eintr.springframework.web.method.annotation;

import org.eintr.springframework.annotation.mvc.RequestParam;
import org.eintr.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.eintr.springframework.core.MethodParameter;
import org.eintr.springframework.web.context.request.ServletWebRequest;
import org.eintr.springframework.web.servlet.mvc.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.eintr.springframework.web.servlet.mvc.method.annotation.ValueConstants;

import javax.servlet.http.HttpServletRequest;

public class RequestParamMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {
    public RequestParamMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, ServletWebRequest request) throws Exception {
        HttpServletRequest servletRequest = request.getRequest();
        if (servletRequest != null) {

        }
        Object arg = null;
        String[] paramValues = servletRequest.getParameterValues(name);
        if (paramValues != null) {
            arg = (paramValues.length == 1 ? paramValues[0] : paramValues);
        }
        return arg;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(RequestParam.class)) {
            // TODO 目前是简单地直接判断
            //if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
            //    RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
            //    return (requestParam != null && StringUtils.hasText(requestParam.value()));
            //}
            //else {
            //    return true;
            //}
            return true;
        }
        return false;
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        RequestParam ann = parameter.getParameterAnnotation(RequestParam.class);
        return (ann != null ? new RequestParamNamedValueInfo(ann) : new RequestParamNamedValueInfo());
    }

    private static class RequestParamNamedValueInfo extends NamedValueInfo {

        public RequestParamNamedValueInfo() {
            super("", false, ValueConstants.DEFAULT_NONE);
        }

        public RequestParamNamedValueInfo(RequestParam annotation) {
            super(annotation.value(), annotation.required(), annotation.defaultValue());
        }
    }
}
