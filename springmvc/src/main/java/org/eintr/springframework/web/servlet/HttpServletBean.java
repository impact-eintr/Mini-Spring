package org.eintr.springframework.web.servlet;

import org.eintr.springframework.beans.*;
import org.eintr.springframework.core.env.EnvironmentCapable;
import org.eintr.springframework.core.io.Resource;
import org.eintr.springframework.core.io.ResourceLoader;
import org.eintr.springframework.util.CollectionUtils;
import org.eintr.springframework.util.StringUtils;
import org.eintr.springframework.web.context.support.ServletContextResourceLoader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class HttpServletBean extends HttpServlet implements EnvironmentCapable {
    private final Set<String> requiredProperties = new HashSet<>(4);

    protected final void addRequiredProperty(String property) {
        this.requiredProperties.add(property);
    }

    @Override
    public final void init() throws ServletException {
        // Set bean properties from init parameters.
        // 获取 web.xml 中的配置
        PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
        if (!pvs.isEmpty()) {
            try {
                // 将 HttpServletBean 创建
                BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
                // 资源加载器创建,核心对象是 ServletContext
                ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
                // 注册自定义编辑器
                //bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, getEnvironment()));
                // 实例化 BeanWrapper
                initBeanWrapper(bw);
                // BeanWrapper 设置属性
                bw.setPropertyValues(pvs, true);

                // TODO 这里是存储这个 spring-mvc.xml 的内容 不需要启动cocntext机制
                // 后续将会在webApplicatioContext中启动 这个context依赖于 spring-mvc.xml
                // BeanWrapper 不需要实现任何东西 只要吧这个xml保存下来就行
            }
            catch (BeansException ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        // Let subclasses do whatever initialization they like.
        // 实例化 ServletBean
        initServletBean();
    }


    protected void initBeanWrapper(BeanWrapper bw) throws BeansException {
    }

    protected void initServletBean() throws ServletException {
    }


    private static class ServletConfigPropertyValues extends PropertyValues {

        /**
         * Create new ServletConfigPropertyValues.
         * @param config the ServletConfig we'll use to take PropertyValues from
         * @param requiredProperties set of property names we need, where
         * we can't accept default values
         * @throws ServletException if any required properties are missing
         */
        public ServletConfigPropertyValues(ServletConfig config, Set<String> requiredProperties)
                throws ServletException {

            Set<String> missingProps = (!CollectionUtils.isEmpty(requiredProperties) ?
                    new HashSet<>(requiredProperties) : null);

            Enumeration<String> paramNames = config.getInitParameterNames();
            while (paramNames.hasMoreElements()) {
                String property = paramNames.nextElement();
                Object value = config.getInitParameter(property);
                addPropertyValue(new PropertyValue(property, value));
                if (missingProps != null) {
                    missingProps.remove(property);
                }
            }

            // Fail if we are still missing properties.
            if (!CollectionUtils.isEmpty(missingProps)) {
                throw new ServletException(
                        "Initialization from ServletConfig for servlet '" +
                                config.getServletName() +
                                "' failed; the following required properties were missing: " +
                                StringUtils.collectionToDelimitedString(missingProps, ", "));
            }
        }
    }

}
