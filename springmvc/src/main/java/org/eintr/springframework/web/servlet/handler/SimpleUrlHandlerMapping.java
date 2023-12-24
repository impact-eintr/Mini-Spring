package org.eintr.springframework.web.servlet.handler;

import cn.hutool.core.util.StrUtil;
import org.eintr.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.config.BeanDefinition;
import org.eintr.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleUrlHandlerMapping extends AbstractUrlHandlerMapping {
    private final Map<String, Object> urlMap = new LinkedHashMap<String, Object>();

    public SimpleUrlHandlerMapping() {
        //String defaultServletHandlerName  = getApplicationContext().getBean(DefaultServletHttpRequestHandler.class).getClass().getName();
        //Map<String, String> urlMap = new LinkedHashMap<>();
        //urlMap.put("/**", defaultServletHandlerName);
    }

    public SimpleUrlHandlerMapping(Map<String, ?> urlMap) {
        setUrlMap(urlMap);
    }

    public Map<String, ?> getUrlMap() {
        return urlMap;
    }

    public void setUrlMap(Map<String, ?> urlMap) {
        this.urlMap.putAll(urlMap);
    }

    public void initApplicationContext() throws BeansException {
        super.initApplicationContext();
        // ApplicationContextAware 因为实现了这个 所以会执行一些Process函数 将会调用这个函数
        registerHandlers(this.urlMap);
    }

    protected void registerHandlers(Map<String, Object> urlMap) throws BeansException {
        if (urlMap.isEmpty()) {
            System.out.println("没有路径");
        } else {
            // 循环处理 urlMap
            urlMap.forEach((url, handler) -> {
                if (!url.startsWith("/")) {
                    url = "/" + url;
                }
                // Remove whitespace from handler bean name.
                if (handler instanceof String) {
                    handler = ((String) handler).trim();
                }
                registerHandler(url, handler);
            });
        }
    }

}
