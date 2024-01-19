package org.eintr.springframework.web.method.support;

import org.eintr.springframework.beans.factory.BeanFactory;
import org.eintr.springframework.beans.factory.ListableBeanFactory;
import org.eintr.springframework.core.MethodParameter;
import org.eintr.springframework.util.*;
import org.eintr.springframework.web.context.request.ServletWebRequest;
import org.eintr.springframework.web.method.HandlerMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvocableHandlerMethod extends HandlerMethod {

    private static final Object[] EMPTY_ARGS = new Object[0];

    private HandlerMethodArgumentResolverComposite resolvers =
            new HandlerMethodArgumentResolverComposite();

    public InvocableHandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
        super(beanName, beanFactory, method);
    }

    /**
     * Create an instance from a {@code HandlerMethod}.
     */
    public InvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }


    /**
     * Create an instance from a bean instance and a method.
     */
    public InvocableHandlerMethod(Object bean, Method method) {
        super(bean, method);
    }

    public Object invokeForRequest(ServletWebRequest request, Object... providedArgs) throws Exception {

        // 获取请求参数
        Object[] args = getMethodArgumentValues(request, providedArgs);

        // 执行处理方法
        return doInvoke(args);
    }


    protected Object[] getMethodArgumentValues(ServletWebRequest request, Object... providedArgs) throws Exception {
        // 获取方法参数集合
        MethodParameter[] parameters = getMethodParameters();
        // 判断是否是空数组
        if (ObjectUtils.isEmpty(parameters)) {
            return EMPTY_ARGS;
        }

        // 数据结果对象
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            // 方法参数对象
            MethodParameter parameter = parameters[i];
            // 初始化参数发现器
            //parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
            // 设置属性对象
            args[i] = findProvidedArgument(parameter, providedArgs);
            if (args[i] != null) {
                continue;
            }
            if (!this.resolvers.supportsParameter(parameter)) {
                throw new IllegalStateException(formatArgumentError(parameter, "No suitable resolver"));
            }
            try {
                // 设置属性对象
                args[i] = this.resolvers.resolveArgument(parameter, request);
            } catch (Exception ex) {
                // Leave stack trace for later, exception may actually be resolved and handled...
                throw ex;
            }
        }
        return args;
    }


    protected static Object findProvidedArgument(MethodParameter parameter, Object... providedArgs) {
        if (!ObjectUtils.isEmpty(providedArgs)) {
            for (Object providedArg : providedArgs) {
                // 获取参数类型, 当前数据值是否是该类型
                if (parameter.getParameterType().isInstance(providedArg)) {
                    return providedArg;
                }
            }
        }
        return null;
    }


    protected static String formatArgumentError(MethodParameter param, String message) {
        return "Could not resolve parameter [" + param.getParameterIndex() + "] in " +
                param.getExecutable().toGenericString() +
                (StringUtils.hasText(message) ? ": " + message : "");
    }


    protected Object doInvoke(Object... args) throws Exception {
        ReflectionUtils.makeAccessible(getBridgedMethod());
        try {
            return getBridgedMethod().invoke(getBean(), args);
        }
        catch (IllegalArgumentException ex) {
            assertTargetBean(getBridgedMethod(), getBean(), args);
            String text = (ex.getMessage() != null ? ex.getMessage() : "Illegal argument");
            throw new IllegalStateException(formatInvokeError(text, args), ex);
        }
        catch (InvocationTargetException ex) {
            // Unwrap for HandlerExceptionResolvers ...
            Throwable targetException = ex.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw (RuntimeException) targetException;
            }
            else if (targetException instanceof Error) {
                throw (Error) targetException;
            }
            else if (targetException instanceof Exception) {
                throw (Exception) targetException;
            }
            else {
                throw new IllegalStateException(formatInvokeError("Invocation failure", args), targetException);
            }
        }
    }

    public void setHandlerMethodArgumentResolvers(
            HandlerMethodArgumentResolverComposite argumentResolvers) {
        this.resolvers = argumentResolvers;
    }
}
