package org.eintr.springframework.web.servlet.mvc.method.annotation;

import org.eintr.springframework.annotation.mvc.RequestMapping;
import org.eintr.springframework.annotation.stereotype.Controller;
import org.eintr.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.eintr.springframework.web.servlet.handler.MatchableHandlerMapping;
import org.eintr.springframework.web.servlet.handler.RequestMatchResult;
import org.omg.CORBA.Request;

import javax.servlet.http.HttpServletRequest;

public class RequestMappingHandlerMethodMapping extends AbstractHandlerMethodMapping<Request>
        implements MatchableHandlerMapping {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return (beanType.isAnnotationPresent(Controller.class)) ||
            (beanType.isAnnotationPresent(RequestMapping.class));
    }

    @Override
    public RequestMatchResult match(HttpServletRequest request, String pattern) {
        return null;
    }
}
