package org.eintr.springframework.web.servlet.handler;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractUrlHandlerMapping extends AbstractHandlerMapping
        implements MatchableHandlerMapping{

    private final Map<String, Object> handlerMap = new LinkedHashMap<>();

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        return null;
    }

    @Override
    public RequestMatchResult match(HttpServletRequest request, String pattern) {
        return null;
    }

    protected void registerHandler(String urlPath, Object handler)
            throws BeansException, IllegalStateException {

        Assert.notNull(urlPath, "URL path must not be null");
        Assert.notNull(handler, "Handler object must not be null");
        Object resolvedHandler = handler;

        if (handler instanceof String) {
            String handlerName = (String) handler;
            ApplicationContext applicationContext = getApplicationContext();
            // 从容器中获取 handler 对象
            resolvedHandler = applicationContext.getBean(handlerName);
        }

        Object mappedHandler = this.handlerMap.get(urlPath);
        if (mappedHandler != null) {

        } else {
            System.out.println("没有找到处理逻辑");
            // TODO
        }
    }
}
