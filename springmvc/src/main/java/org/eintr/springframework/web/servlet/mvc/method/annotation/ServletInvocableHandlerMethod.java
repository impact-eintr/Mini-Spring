package org.eintr.springframework.web.servlet.mvc.method.annotation;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.http.HttpStatus;
import org.eintr.springframework.util.StringUtils;
import org.eintr.springframework.web.context.request.ServletWebRequest;
import org.eintr.springframework.web.method.HandlerMethod;
import org.eintr.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.eintr.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.eintr.springframework.web.method.support.InvocableHandlerMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class ServletInvocableHandlerMethod extends InvocableHandlerMethod {

    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;


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


    public void invokeAndHandle(ServletWebRequest webRequest, Object... providedArgs) throws Exception {
        // 处理请求
        Object returnValue = invokeForRequest(webRequest, providedArgs);
        setResponseStatus(webRequest);
        if (returnValue == null) {
            // TODO
            //if (isRequestNotModified(webRequest) || getResponseStatus() != null) {
            //    // 进行缓存禁用的处理
            //    disableContentCachingIfNecessary(webRequest);
            //    return;
            //}
        }
        else if (StringUtils.hasText(getResponseStatusReason())) {
            return;
        }

        Assert.state(this.returnValueHandlers != null, "No return value handlers");
        try {
            // 返回对象处理
            this.returnValueHandlers.handleReturnValue(
                    returnValue, getReturnValueType(returnValue), webRequest);
        } catch (Exception ex) {
            throw ex;
        }
    }


    private void setResponseStatus(ServletWebRequest webRequest) throws IOException {
        HttpStatus status = getResponseStatus();
        if (status == null) {
            return;
        }

        HttpServletResponse response = webRequest.getResponse();
        if (response != null) {
            String reason = getResponseStatusReason();
            if (StringUtils.hasText(reason)) {
                response.sendError(status.value(), reason);
            }
            else {
                response.setStatus(status.value());
            }
        }
    }


    public void setHandlerMethodReturnValueHandlers(
            HandlerMethodReturnValueHandlerComposite returnValueHandlers) {
        this.returnValueHandlers = returnValueHandlers;
    }
}
