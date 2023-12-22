package org.eintr.springframework.web.servlet;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.context.ApplicationContext;
import org.eintr.springframework.context.ApplicationContextAware;
import org.eintr.springframework.context.ApplicationListener;
import org.eintr.springframework.context.ConfigurableApplicationContext;
import org.eintr.springframework.context.event.ContextRefreshedEvent;
import org.eintr.springframework.util.BeanUtils;
import org.eintr.springframework.web.context.ConfigurableWebApplicationContext;
import org.eintr.springframework.web.context.WebApplicationContext;
import org.eintr.springframework.web.context.support.WebApplicationContextUtils;
import org.eintr.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletException;

public class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {

    public static final Class<?> DEFAULT_CONTEXT_CLASS = XmlWebApplicationContext.class;


    private Class<?> contextClass = DEFAULT_CONTEXT_CLASS;

    private WebApplicationContext webApplicationContext;

    private String contextAttribute;

    private String contextConfigLocation;

    private final Object onRefreshMonitor = new Object();

    private volatile boolean refreshEventReceived = false;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }


    public Class<?> getContextClass() {
        return this.contextClass;
    }

    public void setContextClass(Class<?> contextClass) {
        this.contextClass = contextClass;
    }

    public String getContextAttribute() {
        return this.contextAttribute;
    }

    protected void initServletBean() throws ServletException {
        try {
            this.webApplicationContext = initWebApplicationContext();
            initFrameworkServlet();
        } catch (ServletException | RuntimeException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext rootContext =
                WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        WebApplicationContext wac = null;

        if (this.webApplicationContext != null) {
            // TODO
        }

        if (wac == null) {
            wac = findWebApplicationContext();
        }
        if (wac == null) {
            wac = createWebApplicationContext(rootContext);
        }

        // 刷新应用上下文 TODO
        if (!this.refreshEventReceived) { //不是第一次刷新 会调用这里
            synchronized (this.onRefreshMonitor) {
                onRefresh(wac);
            }
        }

        return wac;
    }


    protected WebApplicationContext findWebApplicationContext() {
        // 获取属性名称
        String attrName = getContextAttribute();
        if (attrName == null) {
            return null;
        }
        // servletContext 中寻找 attrName 的 webApplicationContext
        WebApplicationContext wac =
                WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
        if (wac == null) {
            throw new IllegalStateException("No WebApplicationContext found: initializer not registered?");
        }
        return wac;
    }

    protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent) {
        return createWebApplicationContext((ApplicationContext) parent);
    }

    protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
        Class<?> contextClass = getContextClass();

        ConfigurableWebApplicationContext wac =
                (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
        if (parent != null) {
            wac.setParent(parent);
        }

        String configLocation = getContextConfigLocation();
        if (configLocation != null) {
            wac.setConfigLocation(configLocation);
        }
        // 配置并刷新上下文
        configureAndRefreshWebApplicationContext(wac);
        return wac;
    }


    public String getContextConfigLocation() {
        return this.contextConfigLocation;
    }

    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {

        // 设置 ServletContext
        wac.setServletContext(getServletContext());
        // 设置 ServletConfig
        wac.setServletConfig(getServletConfig());
        // 添加应用监听器
        wac.addApplicationListener(new ContextRefreshListener());

        // 配置 application 属性 TODO 空方法
        WebApplicationContextUtils.initServletPropertySources(
                wac.getServletContext(),
                wac.getServletConfig());
        wac.setConfigLocation(getServletConfig().getInitParameter("ContextConfigLocation"));

        // web应用上下文的后置处理 TODO 空方法
        postProcessWebApplicationContext(wac);

        // 实例化 TODO 空方法
        applyInitializers(wac);

        wac.refresh();
    }

    protected void initFrameworkServlet() throws ServletException {}

    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.refreshEventReceived = true;
        synchronized (this.onRefreshMonitor) {
            onRefresh(event.getApplicationContext());
        }
    }

    protected void postProcessWebApplicationContext(ConfigurableWebApplicationContext wac) {
    }



    protected void applyInitializers(ConfigurableApplicationContext wac) {
        // 获取初始化参数
        // 提取 globalInitializerClasses 参数
        //String globalClassNames = getServletContext().getInitParameter(ContextLoader.GLOBAL_INITIALIZER_CLASSES_PARAM);
        //System.out.println(globalClassNames);
        //if (globalClassNames != null) {
        //    // 循环加载类 从类名转换到ApplicationContextInitializer对象
        //    for (String className : StringUtils.tokenizeToStringArray(globalClassNames, INIT_PARAM_DELIMITERS)) {
        //        this.contextInitializers.add(loadInitializer(className, wac));
        //    }
        //}

        // 如果当前 contextInitializerClasses 字符串存在则进行实例化
        //if (this.contextInitializerClasses != null) {
        //    for (String className : StringUtils.tokenizeToStringArray(this.contextInitializerClasses, INIT_PARAM_DELIMITERS)) {
        //        this.contextInitializers.add(loadInitializer(className, wac));
        //    }
        //}

        // 循环调用 ApplicationContextInitializer
        //for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : this.contextInitializers) {
        //    initializer.initialize(wac);
        //}
    }

    protected void onRefresh(ApplicationContext context) {

    }

    private class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            FrameworkServlet.this.onApplicationEvent(event);
        }
    }
}
