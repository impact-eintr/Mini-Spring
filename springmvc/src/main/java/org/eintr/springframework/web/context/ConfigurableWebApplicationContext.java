package org.eintr.springframework.web.context;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.context.ApplicationContext;
import org.eintr.springframework.context.ConfigurableApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;


public interface ConfigurableWebApplicationContext
        extends WebApplicationContext, ConfigurableApplicationContext {
    /**
     * Prefix for ApplicationContext ids that refer to context path and/or servlet name.
     * 应用上下文 id 前缀
     */
    String APPLICATION_CONTEXT_ID_PREFIX = WebApplicationContext.class.getName() + ":";

    /**
     * Name of the ServletConfig environment bean in the factory.
     * servlet 配置名称(bean name)
     * @see javax.servlet.ServletConfig
     */
    String SERVLET_CONFIG_BEAN_NAME = "servletConfig";


    /**
     * Set the ServletContext for this web application context.
     * <p>Does not cause an initialization of the context: refresh needs to be
     * called after the setting of all configuration properties.
     *
     * 设置 servlet 上下文
     */
    void setServletContext(ServletContext servletContext);

    /**
     * Set the ServletConfig for this web application context.
     * Only called for a WebApplicationContext that belongs to a specific Servlet.
     * 设置 servlet config
     */
    void setServletConfig(ServletConfig servletConfig);

    /**
     * Return the ServletConfig for this web application context, if any.
     *
     * 获取 servlet config
     */
    ServletConfig getServletConfig();

    /**
     * Set the config locations for this web application context in init-param style,
     * i.e. with distinct locations separated by commas, semicolons or whitespace.
     * <p>If not set, the implementation is supposed to use a default for the
     * given namespace or the root web application context, as appropriate.
     *
     * 设置配置文件地址
     */
    void setConfigLocation(String configLocation);

    /**
     * Set the config locations for this web application context.
     * <p>If not set, the implementation is supposed to use a default for the
     * given namespace or the root web application context, as appropriate.
     *
     * 设置配置文件地址
     */
    void setConfigLocations(String... configLocations);

    /**
     * Return the config locations for this web application context,
     * or {@code null} if none specified.
     *
     * 获取配置文件地址
     */
    String[] getConfigLocations();

}
