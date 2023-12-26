package org.eintr.springframework.web.servlet.handler;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.BeanNameAware;
import org.eintr.springframework.util.BeanFactoryUtils;
import org.eintr.springframework.web.context.support.WebApplicationObjectSupport;
import org.eintr.springframework.web.servlet.HandlerExecutionChain;
import org.eintr.springframework.web.servlet.HandlerInterceptor;
import org.eintr.springframework.web.servlet.HandlerMapping;
import org.eintr.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHandlerMapping extends WebApplicationObjectSupport
        implements HandlerMapping, BeanNameAware {

    private Object rootHandler;

    protected UrlPathHelper urlPathHelper = new UrlPathHelper();

    private final List<Object> interceptors = new ArrayList<>();

    private final List<HandlerInterceptor> adaptedInterceptors = new ArrayList<>();

    private Object defaultHandler;

    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    protected abstract Object getHandlerInternal(HttpServletRequest request) throws Exception;

    public Object getDefaultHandler() {
        return this.defaultHandler;
    }


    public void setDefaultHandler(Object defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public Object getRootHandler() {
        return this.rootHandler;
    }


    public void setRootHandler(Object handler) {
        this.rootHandler = handler;
    }

    protected void initApplicationContext() throws BeansException {
        System.out.println("初始化 Context");
        // 将会设置一些东西
        extendInterceptors(this.interceptors);
        detectMappedInterceptors(this.adaptedInterceptors);
        initInterceptors();
    }


    protected void extendInterceptors(List<Object> interceptors) {
    }


    protected void detectMappedInterceptors(List<HandlerInterceptor> mappedInterceptors) {
        mappedInterceptors.addAll(
                BeanFactoryUtils.beansOfTypeIncludingAncestors(
                        getApplicationContext(),
                        MappedInterceptor.class).values());
    }


    protected void initInterceptors() {
        if (!this.interceptors.isEmpty()) {
            for (int i = 0; i < this.interceptors.size(); i++) {
                Object interceptor = this.interceptors.get(i);
                if (interceptor == null) {
                    throw new IllegalArgumentException("Entry number " + i + " in interceptors array is null");
                }
                //this.adaptedInterceptors.add(adaptInterceptor(interceptor));
            }
        }
    }

    @Override
    public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        // 将 request 进行搜索找到对应的 handler 对象
        Object handler = getHandlerInternal(request);
        if (handler == null) {
            handler = getDefaultHandler();
        }
        if (handler == null) {
            return null;
        }

        HandlerExecutionChain executionChain = null;

        return executionChain;
    }
}
