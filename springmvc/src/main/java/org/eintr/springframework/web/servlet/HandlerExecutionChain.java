package org.eintr.springframework.web.servlet;

import org.eintr.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class HandlerExecutionChain {

    /**
     * 处理器对象
     */
    private final Object handler;

    /**
     * 拦截器列表
     */
    private HandlerInterceptor[] interceptors;

    /**
     * 拦截器列表
     */
    private List<HandlerInterceptor> interceptorList;

    public HandlerExecutionChain(Object handler) {
        this(handler, (HandlerInterceptor[]) null);
    }


    public HandlerExecutionChain(Object handler, HandlerInterceptor... interceptors) {
        if (handler instanceof HandlerExecutionChain) {
            HandlerExecutionChain originalChain = (HandlerExecutionChain) handler;
            this.handler = originalChain.getHandler();
            this.interceptorList = new ArrayList<>();
            CollectionUtils.mergeArrayIntoCollection(originalChain.getInterceptors(), this.interceptorList);
            CollectionUtils.mergeArrayIntoCollection(interceptors, this.interceptorList);
        } else {
            this.handler = handler;
            this.interceptors = interceptors;
        }
    }

    public Object getHandler() {
        return this.handler;
    }


    public HandlerInterceptor[] getInterceptors() {
        if (this.interceptors == null && this.interceptorList != null) {
            this.interceptors = this.interceptorList.toArray(new HandlerInterceptor[0]);
        }
        return this.interceptors;
    }
}
