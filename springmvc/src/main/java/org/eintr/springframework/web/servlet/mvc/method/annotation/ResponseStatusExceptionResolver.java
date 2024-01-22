package org.eintr.springframework.web.servlet.mvc.method.annotation;

import org.eintr.springframework.web.servlet.ModelAndView;
import org.eintr.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResponseStatusExceptionResolver  extends AbstractHandlerExceptionResolver {
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        return null;
    }
}
