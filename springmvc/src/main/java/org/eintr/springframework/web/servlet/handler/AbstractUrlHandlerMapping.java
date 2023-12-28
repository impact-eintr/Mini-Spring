package org.eintr.springframework.web.servlet.handler;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.context.ApplicationContext;
import org.eintr.springframework.util.CollectionUtils;
import org.eintr.springframework.web.servlet.HandlerExecutionChain;
import org.eintr.springframework.web.servlet.HandlerMapping;
import org.eintr.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractUrlHandlerMapping extends AbstractHandlerMapping
        implements MatchableHandlerMapping{

    private final Map<String, Object> handlerMap = new LinkedHashMap<>();

    String LOOKUP_PATH = HandlerMapping.class.getName() + ".lookupPath";

    // 寻找处理器
    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        // 提取请求地址
        String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
        // 设置请求地址
        request.setAttribute(LOOKUP_PATH, lookupPath);
        // 查询 handler 对象
        Object handler = lookupHandler(lookupPath, request);
        if (handler == null) {
            Object rawHandler = null;
            // 对于访问路径是 / 的处理
            if ("/".equals(lookupPath)) {
                // 获取根Handler对象
                rawHandler = getRootHandler();
            }
            // handler 还是空
            if (rawHandler == null) {
                // 获取默认的Handler对象
                rawHandler = getDefaultHandler();
            }
            // rawHandler 不为空
        }

        return handler;
    }




    protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
        Object handler = this.handlerMap.get(urlPath);
        if (handler != null) {
            if (handler instanceof String) {
                String handlerName = (String) handler;
                handler = getApplicationContext().getBean(handlerName);
            }

            // 验证handler对象
            validateHandler(handler, request);
            // 创建 handler 对象
            return buildPathExposingHandler(handler, urlPath, urlPath, null);
        }

        return null;
    }


    protected void validateHandler(Object handler, HttpServletRequest request) throws Exception {
    }


    protected Object buildPathExposingHandler(Object rawHandler, String bestMatchingPattern,
                                              String pathWithinMapping, Map<String, String> uriTemplateVariables) {

        return null;
    }

    @Override
    public RequestMatchResult match(HttpServletRequest request, String pattern) {
        return null;
    }


    protected void registerHandler(String[] urlPaths, String beanName) throws BeansException, IllegalStateException {
        Assert.notNull(urlPaths, "URL path array must not be null");
        for (String urlPath : urlPaths) {
            registerHandler(urlPath, beanName);
        }
    }

    protected void registerHandler(String urlPath, Object handler)
            throws BeansException, IllegalStateException {

        Assert.notNull(urlPath, "URL path must not be null");
        Assert.notNull(handler, "Handler object must not be null");
        Object resolvedHandler = handler;

        if (handler instanceof String) { // handler 是一个String 获取真实的类
            String handlerName = (String) handler;
            ApplicationContext applicationContext = getApplicationContext();
            // 从容器中获取 handler 对象
            resolvedHandler = applicationContext.getBean(handlerName);
        }

        Object mappedHandler = this.handlerMap.get(urlPath);
        if (mappedHandler != null) {

            if (mappedHandler != resolvedHandler) {
                throw new IllegalStateException(
                        "Cannot map to URL path [" + urlPath +
                                "]: There is already mapped.");
            }
        } else {
            if (urlPath.equals("/")) {
                setRootHandler(resolvedHandler);
            }
            else if (urlPath.equals("/*")) {
                setDefaultHandler(resolvedHandler);
            }
            else {
                this.handlerMap.put(urlPath, resolvedHandler);
            }
        }
    }
}
