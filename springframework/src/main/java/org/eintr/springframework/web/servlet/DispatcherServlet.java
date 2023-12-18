package org.eintr.springframework.web.servlet;

import org.eintr.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServlet;

public class DispatcherServlet extends FrameworkServlet {



    protected void initStrategies(ApplicationContext context) {
        // 初始化 MultipartResolver
        initMultipartResolver(context);
        // 初始化 LocaleResolver
        initLocaleResolver(context);
        // 初始化 ThemeResolver
        initThemeResolver(context);
        // 初始化 HandlerMappings --
        initHandlerMappings(context);
        // 初始化 HandlerAdapters --
        initHandlerAdapters(context);
        // 初始化 HandlerExceptionResolvers --
        initHandlerExceptionResolvers(context);
        // 初始化 RequestToViewNameTranslator
        initRequestToViewNameTranslator(context);
        // 初始化 ViewResolvers --
        initViewResolvers(context);
        // 初始化 FlashMapManager
        initFlashMapManager(context);
    }

    // 初始化 MultipartResolver
    public void initMultipartResolver(ApplicationContext context) {

    }
    // 初始化 LocaleResolver
    public void initLocaleResolver(ApplicationContext context) {
    }

    // 初始化 ThemeResolver
    public void initThemeResolver(ApplicationContext context) {
    }

    // 初始化 HandlerMappings
    public void initHandlerMappings(ApplicationContext context) {
    }

    // 初始化 HandlerAdapters
    public void initHandlerAdapters(ApplicationContext context) {
    }

    // 初始化 HandlerExceptionResolvers
    public void initHandlerExceptionResolvers(ApplicationContext context) {
    }

    // 初始化 RequestToViewNameTranslator
    public void initRequestToViewNameTranslator(ApplicationContext context) {
    }

    // 初始化 ViewResolvers --
    public void initViewResolvers(ApplicationContext context) {
    }

    // 初始化 FlashMapManager
    public void initFlashMapManager(ApplicationContext context) {
    }
}

