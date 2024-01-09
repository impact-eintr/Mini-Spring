package org.eintr.springframework.web.servlet;

import org.eintr.springframework.beans.factory.BeanInitializationException;
import org.eintr.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.eintr.springframework.context.ApplicationContext;
import org.eintr.springframework.core.io.ClassPathResource;
import org.eintr.springframework.util.*;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DispatcherServlet extends FrameworkServlet {

    /** Well-known name for the MultipartResolver object in the bean factory for this namespace. */
    public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";

    /** Well-known name for the LocaleResolver object in the bean factory for this namespace. */
    public static final String LOCALE_RESOLVER_BEAN_NAME = "localeResolver";

    /** Well-known name for the ThemeResolver object in the bean factory for this namespace. */
    public static final String THEME_RESOLVER_BEAN_NAME = "themeResolver";

    /**
     * Well-known name for the HandlerMapping object in the bean factory for this namespace.
     * Only used when "detectAllHandlerMappings" is turned off.
     */
    public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";

    /**
     * Well-known name for the HandlerAdapter object in the bean factory for this namespace.
     * Only used when "detectAllHandlerAdapters" is turned off.
     */
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";

    /**
     * Well-known name for the HandlerExceptionResolver object in the bean factory for this namespace.
     * Only used when "detectAllHandlerExceptionResolvers" is turned off.
     */
    public static final String HANDLER_EXCEPTION_RESOLVER_BEAN_NAME = "handlerExceptionResolver";

    /**
     * Well-known name for the RequestToViewNameTranslator object in the bean factory for this namespace.
     */
    public static final String REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME = "viewNameTranslator";

    /**
     * Well-known name for the ViewResolver object in the bean factory for this namespace.
     * Only used when "detectAllViewResolvers" is turned off.
     */
    public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";

    /**
     * Well-known name for the FlashMapManager object in the bean factory for this namespace.
     */
    public static final String FLASH_MAP_MANAGER_BEAN_NAME = "flashMapManager";


    /** MultipartResolver used by this servlet. */
    private MultipartResolver multipartResolver;

    /** LocaleResolver used by this servlet. */
    private LocaleResolver localeResolver;

    /** ThemeResolver used by this servlet. */
    private ThemeResolver themeResolver;

    /** List of HandlerMappings used by this servlet. */
    private List<HandlerMapping> handlerMappings;

    /** List of HandlerAdapters used by this servlet. */
    private List<HandlerAdapter> handlerAdapters;

    /** List of HandlerExceptionResolvers used by this servlet. */
    private List<HandlerExceptionResolver> handlerExceptionResolvers;

    /** RequestToViewNameTranslator used by this servlet. */
    private RequestToViewNameTranslator viewNameTranslator;

    /** FlashMapManager used by this servlet. */
    private FlashMapManager flashMapManager;

    /** List of ViewResolvers used by this servlet. */
    private List<ViewResolver> viewResolvers;

    /**
     * Request attribute to hold the current web application context.
     * Otherwise only the global web app context is obtainable by tags etc.
     */
    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

    /**
     * Request attribute to hold the current LocaleResolver, retrievable by views.
     */
    public static final String LOCALE_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".LOCALE_RESOLVER";

    /**
     * Request attribute to hold the current ThemeResolver, retrievable by views.
     */
    public static final String THEME_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_RESOLVER";

    /**
     * Request attribute to hold the current ThemeSource, retrievable by views.
     */
    public static final String THEME_SOURCE_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_SOURCE";

    private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";


    private static final Properties defaultStrategies;

    static {
        // Load default strategy implementations from properties file.
        // This is currently strictly internal and not meant to be customized
        // by application developers.
        try {
            // 读取 DispatcherServlet.properties 文件
            ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH);
            defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Could not load '" + DEFAULT_STRATEGIES_PATH + "': " +
                    ex.getMessage());
        }
    }

    @Override
    protected void onRefresh(ApplicationContext context) {
        initStrategies(context);
    }



    protected void initStrategies(ApplicationContext context) {
        // FIXME
        //initMultipartResolver(context);
        //initLocaleResolver(context);
        //initThemeResolver(context);

        // 初始化 HandlerMappings
        initHandlerMappings(context);

        // 初始化 HandlerAdapters
        initHandlerAdapters(context);

        // 初始化 HandlerExceptionResolvers
        initHandlerExceptionResolvers(context);

        initRequestToViewNameTranslator(context);
        initViewResolvers(context);
        initFlashMapManager(context);
    }


    /**
     * Initialize the MultipartResolver used by this class.
     * <p>If no bean is defined with the given name in the BeanFactory for this namespace,
     * no multipart handling is provided.
     */
    private void initMultipartResolver(ApplicationContext context) {
        try {
            this.multipartResolver = context.getBean(MULTIPART_RESOLVER_BEAN_NAME, MultipartResolver.class);
        }
        catch (NoSuchBeanDefinitionException ex) {
            // Default is no multipart resolver.
            this.multipartResolver = null;
        }
    }

    /**
     * Initialize the LocaleResolver used by this class.
     * <p>If no bean is defined with the given name in the BeanFactory for this namespace,
     * we default to AcceptHeaderLocaleResolver.
     */
    private void initLocaleResolver(ApplicationContext context) {
        try {
            // 从Spring容器中根据名称和类型获取
            this.localeResolver = context.getBean(LOCALE_RESOLVER_BEAN_NAME, LocaleResolver.class);
        }
        catch (NoSuchBeanDefinitionException ex) {
            // We need to use the default.
            // 加载默认的 LocaleResolver
            this.localeResolver = getDefaultStrategy(context, LocaleResolver.class);

        }
    }

    /**
     * Initialize the ThemeResolver used by this class.
     * <p>If no bean is defined with the given name in the BeanFactory for this namespace,
     * we default to a FixedThemeResolver.
     */
    private void initThemeResolver(ApplicationContext context) {
        try {
            this.themeResolver = context.getBean(THEME_RESOLVER_BEAN_NAME, ThemeResolver.class);
        }
        catch (NoSuchBeanDefinitionException ex) {
            // We need to use the default.
            this.themeResolver = getDefaultStrategy(context, ThemeResolver.class);

        }
    }

    // 初始化 HandlerMappings
    public void initHandlerMappings(ApplicationContext context) {
        // 用于根据request找到对应的处理器 Handler he  Interceptors
        this.handlerMappings = null;

        try {
            // 检查用户是否自定义了处理器
            HandlerMapping hm = context.getBean(HANDLER_MAPPING_BEAN_NAME, HandlerMapping.class);
            this.handlerMappings = Collections.singletonList(hm);
        } catch (NoSuchBeanDefinitionException ex) {
        }

        if (this.handlerMappings == null) { // 用默认的
            this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
        }
    }

    // 初始化 HandlerAdapters
    public void initHandlerAdapters(ApplicationContext context) {
        // 用于处理请求
        this.handlerAdapters = null;
        Map<String, HandlerAdapter> matchingBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class);
        if (!matchingBeans.isEmpty()) {
          this.handlerAdapters = new ArrayList<>(matchingBeans.values());
        }

        if (this.handlerAdapters == null) {
            this.handlerAdapters = getDefaultStrategies(context, HandlerAdapter.class);
        }
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


    protected <T> T getDefaultStrategy(ApplicationContext context, Class<T> strategyInterface) {
        List<T> strategies = getDefaultStrategies(context, strategyInterface);
        if (strategies.size() != 1) {
            throw new BeanInitializationException(
                    "DispatcherServlet needs exactly 1 strategy for interface [" +
                            strategyInterface.getName() + "]");
        }
        return strategies.get(0);
    }


    protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface) {
        // 获取类名
        String key = strategyInterface.getName();
        // 获取属性值
        String value = defaultStrategies.getProperty(key);
        if (value != null) {
            // 将属性值进行拆分
            String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
            List<T> strategies = new ArrayList<>(classNames.length);
            for (String className : classNames) {
                try {
                    // 反射获取类
                    Class<?> clazz = ClassUtils.forName(className, DispatcherServlet.class.getClassLoader());
                    // 创建对象
                    Object strategy = createDefaultStrategy(context, clazz);
                    strategies.add((T) strategy);
                } catch (ClassNotFoundException ex) {
                    throw new BeanInitializationException(
                            "Could not find DispatcherServlet's default strategy class [" + className +
                                    "] for interface [" + key + "]", ex);
                } catch (LinkageError err) {
                    throw new BeanInitializationException(
                            "Unresolvable class definition for DispatcherServlet's default strategy class [" +
                                    className + "] for interface [" + key + "]", err);
                }
            }
            return strategies;
        } else {
            return new LinkedList<>();
        }
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logRequest(request);


        // Make framework objects available to handlers and view objects.
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
        request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
        request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
        //request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());

        try {
            doDispatch(request, response);
        } finally {

        }
    }


    private void logRequest(HttpServletRequest request) {
        String params;
        params = request.getParameterMap().entrySet().stream()
                .map(entry -> entry.getKey() + ":" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", "));
        String queryString = request.getQueryString();
        String queryClause = (StringUtils.hasLength(queryString) ? "?" + queryString : "");
        String dispatchType = (!request.getDispatcherType().equals(DispatcherType.REQUEST) ?
                "\"" + request.getDispatcherType().name() + "\" dispatch for " : "");
        String message = (dispatchType + request.getMethod() + " \"" + getRequestUri(request) +
                queryClause + "\", parameters={" + params + "}");
        System.out.println(message);
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest processedRequest = request;
        HandlerExecutionChain mappedHandler = null;
        try {

            Exception dispatchException = null;
            try {

                // 将请求转换为 handler 对象
                mappedHandler = getHandler(processedRequest);
                if (mappedHandler == null) {
                    noHandlerFound(processedRequest, response);
                    return;
                }
                // handler 对象转换成 HandlerAdapter 对象
                // Determine handler adapter for the current request.
                HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

                String method = request.getMethod();
                boolean isGet = "GET".equals(method);
                if (isGet || "HEAD".equals(method)) {
                    long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                    //if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
                    //    return;
                    //}
                }

                // TODO 处理拦截器

                // 执行 HandlerAdapter的handle方法
                ha.handle(processedRequest, response, mappedHandler.getHandler());

            } catch (Exception ex) {
                dispatchException = ex;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {

        }

    }


    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (this.handlerMappings != null) {
            for (HandlerMapping mapping : this.handlerMappings) {
                HandlerExecutionChain handler = mapping.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }

    protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        if (this.handlerAdapters != null) {
            for (HandlerAdapter adapter : this.handlerAdapters) {
                if (adapter.supports(handler)) {
                    return adapter;
                }
            }
        }
        throw new ServletException("No adapter for handler [" + handler +
                "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
    }


    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }


    protected Object createDefaultStrategy(ApplicationContext context, Class<?> clazz) {
        return context.getAutowireCapableBeanFactory().createBean(clazz);
    }

    private static String getRequestUri(HttpServletRequest request) {
        String uri = (String) request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE);
        if (uri == null) {
            uri = request.getRequestURI();
        }
        return uri;
    }
}


