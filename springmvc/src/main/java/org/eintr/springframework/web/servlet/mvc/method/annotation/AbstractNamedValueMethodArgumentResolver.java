package org.eintr.springframework.web.servlet.mvc.method.annotation;

import org.eintr.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.eintr.springframework.core.MethodParameter;
import org.eintr.springframework.web.context.request.ServletWebRequest;
import org.eintr.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.eintr.springframework.web.servlet.mvc.method.annotation.ValueConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractNamedValueMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final ConfigurableBeanFactory configurableBeanFactory;

    private final Map<MethodParameter, NamedValueInfo> namedValueInfoCache =
            new ConcurrentHashMap<>(256);

    public AbstractNamedValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
        this.configurableBeanFactory = beanFactory;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ServletWebRequest webRequest) throws Exception {
        NamedValueInfo namedValueInfo = getNamedValueInfo(parameter);
        // 进行参数名称解析
        Object resolvedName = resolveStringValue(namedValueInfo.name);
        if (resolvedName == null) {
            throw new IllegalArgumentException(
                    "Specified name must not resolve to null: [" + namedValueInfo.name + "]");
        }

        // 进行数据值解析
        Object arg = resolveName(resolvedName.toString(), parameter, webRequest);
        if (arg == null) {
            if (namedValueInfo.defaultValue != null) {
                arg = resolveStringValue(namedValueInfo.defaultValue);
            }
            else if (namedValueInfo.required) {
                //handleMissingValue(namedValueInfo.name, nestedParameter, webRequest);
            }
            //arg = handleNullValue(namedValueInfo.name, arg, nestedParameter.getNestedParameterType());
        }
        else if ("".equals(arg) && namedValueInfo.defaultValue != null) {
            arg = resolveStringValue(namedValueInfo.defaultValue);
        }
        return arg;
    }

    private NamedValueInfo getNamedValueInfo(MethodParameter parameter) {
        NamedValueInfo namedValueInfo = this.namedValueInfoCache.get(parameter);
        if (namedValueInfo == null) {
            namedValueInfo = createNamedValueInfo(parameter);
            namedValueInfo = updateNamedValueInfo(parameter, namedValueInfo);
            this.namedValueInfoCache.put(parameter, namedValueInfo);
        }
        return namedValueInfo;
    }

    protected abstract Object resolveName(String name, MethodParameter parameter,
                                          ServletWebRequest request) throws Exception;

    private Object resolveStringValue(String value) {
        if (this.configurableBeanFactory == null) {
            return value;
        }
        return this.configurableBeanFactory.resolveEmbeddedValue(value);
    }


    protected abstract NamedValueInfo createNamedValueInfo(MethodParameter parameter);

    /**
     * Create a new NamedValueInfo based on the given NamedValueInfo with sanitized values.
     */
    private NamedValueInfo updateNamedValueInfo(MethodParameter parameter, NamedValueInfo info) {
        String name = info.name;
        if (info.name.isEmpty()) {
            name = parameter.getParameterName();
            if (name == null) {
                throw new IllegalArgumentException(
                        "Name for argument type [" + parameter.getParameterType().getName() +
                                "] not available, and parameter name information not found in class file either.");
            }
        }
        String defaultValue = (ValueConstants.DEFAULT_NONE.equals(info.defaultValue) ? null : info.defaultValue);
        return new NamedValueInfo(name, info.required, defaultValue);
    }

    protected static class NamedValueInfo {

        private final String name;

        private final boolean required;

        private final String defaultValue;

        public NamedValueInfo(String name, boolean required, String defaultValue) {
            this.name = name;
            this.required = required;
            this.defaultValue = defaultValue;
        }
    }
}
