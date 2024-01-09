package org.eintr.springframework.web.servlet.handler;

import cn.hutool.core.util.StrUtil;
import org.eintr.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.config.BeanDefinition;
import org.eintr.springframework.util.CollectionUtils;
import org.eintr.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class SimpleUrlHandlerMapping extends AbstractUrlHandlerMapping {
    private final LinkedHashMap<String, Object> urlMap = new LinkedHashMap<>();

    public SimpleUrlHandlerMapping() {
    }

    public SimpleUrlHandlerMapping(Map<String, ?> urlMap) {
        setUrlMap(urlMap);
    }


    public void setMappings(Properties mappings) {
        CollectionUtils.mergePropertiesIntoMap(mappings, this.urlMap);
    }


    public Properties getMappings() {
        Properties properties = new Properties();
        this.urlMap.forEach((key,value) -> {
            if (value instanceof String) {
                properties.setProperty(key, (String) value);
            }
        });
        return properties;
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
