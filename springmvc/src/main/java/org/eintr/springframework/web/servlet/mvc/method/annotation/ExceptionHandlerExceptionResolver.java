package org.eintr.springframework.web.servlet.mvc.method.annotation;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.InitializingBean;
import org.eintr.springframework.context.ApplicationContext;
import org.eintr.springframework.context.ApplicationContextAware;
import org.eintr.springframework.web.method.HandlerMethod;
import org.eintr.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.eintr.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.eintr.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.eintr.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.eintr.springframework.web.servlet.ModelAndView;
import org.eintr.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ExceptionHandlerExceptionResolver extends AbstractHandlerMethodExceptionResolver
    implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;
    @Override
    public void afterPropertiesSet() throws Exception {
        initExceptionHandlerAdviceCache();

        // 参数解析器设置
        if (this.argumentResolvers == null) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
        }
        // 返回值解析器设置
        if (this.returnValueHandlers == null) {
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
        }
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    private void initExceptionHandlerAdviceCache() {
        // TODO
    }

    protected List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        // Annotation-based argument resolution
        resolvers.add(new SessionAttributeMethodArgumentResolver());
        resolvers.add(new RequestAttributeMethodArgumentResolver());

        // Type-based argument resolution
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());
        resolvers.add(new RedirectAttributesMethodArgumentResolver());
        resolvers.add(new ModelMethodProcessor());

        // Custom arguments
        if (getCustomArgumentResolvers() != null) {
            resolvers.addAll(getCustomArgumentResolvers());
        }

        return resolvers;
    }


    protected List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();

        // Single-purpose return value types
        handlers.add(new ModelAndViewMethodReturnValueHandler());
        handlers.add(new ModelMethodProcessor());
        handlers.add(new ViewMethodReturnValueHandler());
        handlers.add(new HttpEntityMethodProcessor(
                getMessageConverters(), this.contentNegotiationManager, this.responseBodyAdvice));

        // Annotation-based return value types
        handlers.add(new ModelAttributeMethodProcessor(false));
        handlers.add(new RequestResponseBodyMethodProcessor(
                getMessageConverters(), this.contentNegotiationManager, this.responseBodyAdvice));

        // Multi-purpose return value types
        handlers.add(new ViewNameMethodReturnValueHandler());
        handlers.add(new MapMethodProcessor());

        // Custom return value types
        if (getCustomReturnValueHandlers() != null) {
            handlers.addAll(getCustomReturnValueHandlers());
        }

        // Catch-all
        handlers.add(new ModelAttributeMethodProcessor(true));

        return handlers;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {
        return null;
    }
}
