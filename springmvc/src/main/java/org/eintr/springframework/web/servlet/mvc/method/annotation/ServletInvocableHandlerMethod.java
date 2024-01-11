package org.eintr.springframework.web.servlet.mvc.method.annotation;

import org.eintr.springframework.beans.factory.BeanFactory;
import org.eintr.springframework.web.method.HandlerMethod;
import org.eintr.springframework.web.method.support.InvocableHandlerMethod;

import javax.servlet.ServletRequest;
import java.lang.reflect.Method;

public class ServletInvocableHandlerMethod extends InvocableHandlerMethod {

    /**
     * Creates an instance from the given handler and method.
     */
    public ServletInvocableHandlerMethod(Object handler, Method method) {
        super(handler, method);
    }

    /**
     * Create an instance from a {@code HandlerMethod}.
     */
    public ServletInvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }

    public void invokeAndHandle(ServletRequest webRequest, Object... providedArgs) throws Exception {
        // 处理请求
        Object returnValue = invokeForRequest(webRequest, providedArgs);

    }

}
