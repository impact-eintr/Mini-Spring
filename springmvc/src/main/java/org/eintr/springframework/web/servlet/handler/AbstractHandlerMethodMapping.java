package org.eintr.springframework.web.servlet.handler;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.eintr.springframework.annotation.mvc.RequestMapping;
import org.eintr.springframework.beans.factory.InitializingBean;
import org.eintr.springframework.util.BeanFactoryUtils;
import org.eintr.springframework.util.BeanUtils;
import org.eintr.springframework.util.ClassUtils;
import org.eintr.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractHandlerMethodMapping<T> extends AbstractHandlerMapping implements InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {
        initHandlerMethods();
    }

    protected abstract boolean isHandler(Class<?> beanType);


    protected void initHandlerMethods() {
        for (String beanName : getCandidateBeanNames()) {
            processCandidateBean(beanName);
        }
        //handlerMethodsInitialized(getHandlerMethods());
    }

    protected String[] getCandidateBeanNames() {
        return getApplicationContext().getBeanNamesForType(Object.class);
    }

    protected void processCandidateBean(String beanName) {
        Class<?> beanType = null;
        try {
            // 获取 BeanName 对应的Bean类型
            beanType = getApplicationContext().getBean(beanName).getClass();
        }
        catch (Throwable ex) {
            ex.printStackTrace();
        }
        // 判断类型是否是 handler对象
        // 主要判断是否具备Controller 或者 RequestMapping 注解
        if (beanType != null && isHandler(beanType)) {
            // 确定 handlerMethod
            detectHandlerMethods(beanName);
        }
    }


    protected void detectHandlerMethods(Object handler) {
        // 获取 handler 的类型
        Class<?> handlerType = (handler instanceof String ?
                getApplicationContext().getBean((String) handler).getClass()
                : handler.getClass());

        if (handlerType != null) {
            //获取类上的@RequestMapping的value值
            String classUri = null;
            // 反射加载类
            //Class<?> userType = ClassUtils.getUserClass(handlerType);
            //Object controllerInstance = null;

            //if (controllerInstance.getClass().isAnnotationPresent(RequestMapping.class)){
            //    classUri = controllerInstance.getClass().getDeclaredAnnotation(RequestMapping.class).value();
            //}

            ////遍历所有的Controller的方法
            //String methodUri;
            //String requestMethod;
            //Request request;
            //for(Method method:controllerInstance.getClass().getDeclaredMethods()) {
            //    //获取controller中所有带@RequestMapping的方法
            //    if(!method.isAnnotationPresent(RequestMapping.class)) {
            //        return;
            //    }

            //    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            //    methodUri = requestMapping.value();
            //    requestMethod = requestMapping.method().name();
            //    //将适用的方法封装成请求体
            //    request = new Request(joinFullUri(classUri,methodUri),requestMethod);

            //    //将handler封装成对象
            //    RequestHandler requestHandler =new RequestHandler(controllerInstance,method);
            //    requestHandlerMap.put(request, requestHandler);
            //}













            //Map<Method, T> methods = MethodIntrospector.selectMethods(userType,
            //        (MethodIntrospector.MetadataLookup<T>) method -> {
            //            try {
            //                // 处理单个 method
            //                return getMappingForMethod(method, userType);
            //            }
            //            catch (Throwable ex) {
            //                throw new IllegalStateException("Invalid mapping on handler class [" +
            //                        userType.getName() + "]: " + method, ex);
            //            }
            //        });

            //// 进行 handlerMethod 注册
            //methods.forEach((method, mapping) -> {
            //    // 获取执行方法,
            //    Method invocableMethod = AopUtils.selectInvocableMethod(method, userType);
            //    registerHandlerMethod(handler, invocableMethod, mapping);
            //});
        }
    }





    private final MappingRegistry mappingRegistry = new MappingRegistry();

    @Override
    protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
        // 获取当前请求路径
        String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
        // 设置属性
        request.setAttribute(LOOKUP_PATH, lookupPath);
        // 上锁
        this.mappingRegistry.acquireReadLock();
        try {
            // 寻找 handler method
            HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
            return null;
            //return (handlerMethod != null ? handlerMethod.createWithResolvedBean() : null);
        }
        finally {
            // 释放锁
            this.mappingRegistry.releaseReadLock();
        }
    }


    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        return null;
    }
        List<Match> matches = new ArrayList<>();

    }

    class MappingRegistry {
        private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        /**
         * Acquire the read lock when using getMappings and getMappingsByUrl.
         */
        public void acquireReadLock() {
            this.readWriteLock.readLock().lock();
        }

        /**
         * Release the read lock after using getMappings and getMappingsByUrl.
         */
        public void releaseReadLock() {
            this.readWriteLock.readLock().unlock();
        }

        public <T> void register(T mapping, Object handler, Method method) {
        }

}
