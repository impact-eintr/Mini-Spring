package org.eintr.springframework.web.servlet.handler;

import org.eintr.springframework.web.servlet.HandlerExceptionResolver;
import org.eintr.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;


public abstract class AbstractHandlerExceptionResolver implements HandlerExceptionResolver {
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private Set<?> mappedHandlers;

    private Class<?>[] mappedHandlerClasses;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 是否可以支持异常处理
        if (shouldApplyTo(request, handler)) {
            // 准备返回对象
            prepareResponse(ex, response);
            // 进行异常解析
            ModelAndView result = doResolveException(request, response, handler, ex);
            if (result != null) {
                // TODO 日志输出
                // Explicitly configured warn logger in logException method.
                //logException(ex, request);
            }
            return result;
        }
        else {
            return null;
        }
    }

    protected boolean shouldApplyTo(HttpServletRequest request, Object handler) {
        if (handler != null) {
            // mappedHandlers中存在handler数据
            if (this.mappedHandlers != null && this.mappedHandlers.contains(handler)) {
                return true;
            }

            if (this.mappedHandlerClasses != null) {
                // mappedHandlerClasses中有handler的父接口
                for (Class<?> handlerClass : this.mappedHandlerClasses) {
                    if (handlerClass.isInstance(handler)) {
                        return true;
                    }
                }
            }
        }
        // Else only apply if there are no explicit handler mappings.
        return (this.mappedHandlers == null && this.mappedHandlerClasses == null);
    }

    public void setMappedHandlerClasses(Class<?>[] mappedHandlerClasses) {
        this.mappedHandlerClasses = mappedHandlerClasses;
    }

    public void setMappedHandlers(Set<?> mappedHandlers) {
        this.mappedHandlers = mappedHandlers;
    }


    protected void prepareResponse(Exception ex, HttpServletResponse response) {
    }


    protected void preventCaching(HttpServletResponse response) {
        response.addHeader(HEADER_CACHE_CONTROL, "no-store");
    }


    protected abstract ModelAndView doResolveException(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);

}
