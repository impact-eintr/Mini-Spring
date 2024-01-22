package org.eintr.springframework.web.servlet.handler;

import org.eintr.springframework.web.method.HandlerMethod;
import org.eintr.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractHandlerMethodExceptionResolver extends AbstractHandlerExceptionResolver {

    @Override
    protected boolean shouldApplyTo(HttpServletRequest request, Object handler) {
        if (handler == null) {
            return super.shouldApplyTo(request, null);
        }
        else if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            handler = handlerMethod.getBean();
            return super.shouldApplyTo(request, handler);
        }
        else {
            return false;
        }
    }

    @Override
    protected final ModelAndView doResolveException(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        return doResolveHandlerMethodException(request, response, (HandlerMethod) handler, ex);
    }


    protected abstract ModelAndView doResolveHandlerMethodException(
            HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex);

}
