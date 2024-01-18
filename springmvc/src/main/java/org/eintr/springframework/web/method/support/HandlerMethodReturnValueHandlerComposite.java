package org.eintr.springframework.web.method.support;

import org.eintr.springframework.core.MethodParameter;
import org.eintr.springframework.web.context.request.ServletWebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler {
    private final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return getReturnValueHandler(returnType) != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ServletWebRequest webRequest) throws Exception {

        // 通过返回值和返回类型寻找对应的 HandlerMethodReturnValueHandler
        HandlerMethodReturnValueHandler handler = selectHandler(returnValue, returnType);
        if (handler == null) {
            throw new IllegalArgumentException("Unknown return value type: " +
                    returnType.getParameterType().getName());
        }
        // 返回值处理
        handler.handleReturnValue(returnValue, returnType, webRequest);
    }


    private HandlerMethodReturnValueHandler selectHandler(
            Object value, MethodParameter returnType) {
        // TODO 同步异步 boolean isAsyncValue = isAsyncReturnValue(value, returnType);
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            //if (isAsyncValue && !(handler instanceof AsyncHandlerMethodReturnValueHandler)) {
            //    continue;
            //}
            if (handler.supportsReturnType(returnType)) {
                return handler;
            }
        }
        return null;
    }


    private HandlerMethodReturnValueHandler getReturnValueHandler(MethodParameter returnType) {
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            if (handler.supportsReturnType(returnType)) {
                return handler;
            }
        }
        return null;
    }


    public HandlerMethodReturnValueHandlerComposite addHandlers(
            List<? extends HandlerMethodReturnValueHandler> handlers) {

        if (handlers != null) {
            this.returnValueHandlers.addAll(handlers);
        }
        return this;
    }

    public List<HandlerMethodReturnValueHandler> getHandlers() {
        return Collections.unmodifiableList(this.returnValueHandlers);
    }
}
