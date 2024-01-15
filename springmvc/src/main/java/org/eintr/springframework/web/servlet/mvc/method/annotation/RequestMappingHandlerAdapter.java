package org.eintr.springframework.web.servlet.mvc.method.annotation;

import org.eintr.springframework.web.method.HandlerMethod;
import org.eintr.springframework.web.servlet.ModelAndView;
import org.eintr.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestMappingHandlerAdapter extends AbstractHandlerMethodAdapter {
    @Override
    protected boolean supportsInternal(HandlerMethod handlerMethod) {
        return true;
    }

    @Override
    protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        ModelAndView mav;
        mav = invokeHandlerMethod(request, response, handlerMethod);
        return mav;
    }


    protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
                                               HttpServletResponse response,
                                               HandlerMethod handlerMethod) throws Exception {

        ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
        // FIXME 设计一个变量把 response 包起来
        invocableMethod.invokeAndHandle(request);

        return null;
    }


    protected ServletInvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        return new ServletInvocableHandlerMethod(handlerMethod);
    }
}
