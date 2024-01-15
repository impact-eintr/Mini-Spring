package org.eintr.springframework.web.servlet.handler;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.annotation.mvc.RequestMapping;
import org.eintr.springframework.annotation.mvc.RequestMethod;
import org.eintr.springframework.beans.factory.InitializingBean;

import org.eintr.springframework.core.convert.support.StringToNumberConverterFactory;
import org.eintr.springframework.util.*;
import org.eintr.springframework.web.context.WebApplicationContext;
import org.eintr.springframework.web.method.HandlerMethod;
import org.eintr.springframework.web.method.RequestMappingInfo;
import org.eintr.springframework.web.servlet.HandlerMapping;
import org.eintr.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractHandlerMethodMapping<T> extends AbstractHandlerMapping
        implements InitializingBean {


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
        Object controllerInstance = (handler instanceof String ?
                getApplicationContext().getBean((String) handler) : handler);
        //获取类上的@RequestMapping的value值
        String[] classUris = null;
        if (controllerInstance.getClass().isAnnotationPresent(RequestMapping.class)){
            classUris = controllerInstance.getClass().getDeclaredAnnotation(RequestMapping.class).value();
        }

        // 获取方法上的 RequestMapping 包括 GetMapping 这些注解
        String[] methodUris;
        RequestMethod requestMethod;
        RequestMappingInfo request;
        for(Method method : controllerInstance.getClass().getDeclaredMethods()) {
            //获取controller中所有带@RequestMapping的方法
            if (!AnnotationUtils.isAnnotationPresent(method, RequestMapping.class)) {
                return;
            }


            RequestMapping requestMapping = AnnotationUtils.getMergeAnnotation(method, RequestMapping.class);
            methodUris = requestMapping.value();
            requestMethod = RequestMethod.valueOf(requestMapping.method().name());
            for (String path : methodUris) {
                if (classUris != null) {
                    for (String classUri : classUris) {
                        request = new RequestMappingInfo(
                                joinFullUri(classUri, path), requestMethod);
                        registerHandlerMethod(handler, method, (T) request);
                    }
                } else {
                    request = new RequestMappingInfo(
                            joinFullUri("", path), requestMethod);
                    registerHandlerMethod(handler, method, (T) request);
                }
            }
        }
    }


    protected void registerHandlerMethod(Object handler, Method method, T mapping) {
        this.mappingRegistry.register(mapping, handler, method);
    }

    /**
     * 对server.base.path、类上的@RequestMapping的value、方法上的@RequestMapping的value进行拼接
     * @param classUri 类上的@RequestMapping的value
     * @param methodUri 方法上的@RequestMapping的value
     * @return
     */
    public String joinFullUri(String classUri,String methodUri) {
        String classPath = WebUtils.formatUrl(classUri);
        String methodPath = WebUtils.formatUrl(methodUri);
        return WebUtils.formatUrl(classPath + methodPath);
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
            return (handlerMethod != null ? handlerMethod.createWithResolvedBean() : null);
        }
        finally {
            // 释放锁
            this.mappingRegistry.releaseReadLock();
        }
    }


    protected HandlerMethod createHandlerMethod(Object handler, Method method) {
        // 是否是字符串
        if (handler instanceof String) {
            // 创建对象
            return new HandlerMethod((String) handler,
                    getApplicationContext().getAutowireCapableBeanFactory(), method);
        }
        return new HandlerMethod(handler, method);
    }

    protected abstract String getMappingPath(T mapping);

    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        List<Match> matches = new ArrayList<>();
        // 从 路由映射表 获取
        List<T> directPathMatches = this.mappingRegistry.getMappingsByUrl(lookupPath);
        // 如果url对应的数据不为空
        if (directPathMatches != null) {
            addMatchingMappings(directPathMatches, matches, request);
        }
        if (matches.isEmpty()) {
            addMatchingMappings(this.mappingRegistry.getMappings().keySet(), matches, request);
        }

        if (!matches.isEmpty()) {

            Comparator<Match> comparator = new MatchComparator(getMappingComparator(request));
            Match bestMatch = matches.get(0);
            if (matches.size() > 1) {

                if (CorsUtils.isPreFlightRequest(request)) {
                    // FIXME 处理跨域
                }

                Match secondBestMatch = matches.get(1);
                // 如果比较结果相同
                if (comparator.compare(bestMatch, secondBestMatch) == 0) {
                    // 第二个元素和第一个元素的比较过程
                    Method m1 = bestMatch.handlerMethod.getMethod();
                    Method m2 = secondBestMatch.handlerMethod.getMethod();
                    String uri = request.getRequestURI();
                    throw new IllegalStateException(
                            "Ambiguous handler methods mapped for '" + uri + "': {" + m1 + ", " + m2 + "}");
                }
            }
            request.setAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE, bestMatch.handlerMethod);
            handleMatch(bestMatch.mapping, lookupPath, request);
            return bestMatch.handlerMethod;
        } else {
            return null;
        }
    }

    /**
     * 添加匹配数据对象
     * @param mappings requestMappingInfo
     * @param matches 匹配表
     * @param request 请求
     */
    private void addMatchingMappings(Collection<T> mappings, List<Match> matches, HttpServletRequest request) {
        for (T mapping : mappings) {
            // 抽象方法
            // 通过抽象方法获取 match 结果
            T match = getMatchingMapping(mapping, request);
            // 是否为空
            if (match != null) {
                // 从 mappingLookup 获取结果并且插入到matches中
                matches.add(new Match(match, this.mappingRegistry.getMappings().get(mapping)));
            }
        }
    }

    protected abstract T getMatchingMapping(T mapping, HttpServletRequest request);

    protected abstract Comparator<T> getMappingComparator(HttpServletRequest request);

    protected void handleMatch(T mapping, String lookupPath, HttpServletRequest request) {
        request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, lookupPath);
    }


    class MappingRegistry {

        /**
         * key:mapping
         * value: mapping registration
         */
        private final Map<T, MappingRegistration<T>> registry = new HashMap<>();

        /**
         * key: mapping
         * value: handlerMethod
         */
        private final Map<T, HandlerMethod> mappingLookup = new LinkedHashMap<>();

        /**
         * key: url
         * value: list mapping
         */
        private final MultiValueMap<String, T> urlLookup = new LinkedMultiValueMap<>();

        /**
         * key: name
         * value: handler method
         */
        private final Map<String, List<HandlerMethod>> nameLookup = new ConcurrentHashMap<>();

        /**
         * key:handler method
         * value: 跨域配置
         */
        //private final Map<HandlerMethod, CorsConfiguration> corsLookup = new ConcurrentHashMap<>();

        /**
         * 读写锁
         */
        private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        /**
         * Return all mappings and handler methods. Not thread-safe.
         * @see #acquireReadLock()
         */
        public Map<T, HandlerMethod> getMappings() {
            return this.mappingLookup;
        }

        /**
         * Return matches for the given URL path. Not thread-safe.
         * @see #acquireReadLock()
         */
        public List<T> getMappingsByUrl(String urlPath) {
            return this.urlLookup.get(urlPath);
        }

        /**
         * Return handler methods by mapping name. Thread-safe for concurrent use.
         */
        public List<HandlerMethod> getHandlerMethodsByMappingName(String mappingName) {
            return this.nameLookup.get(mappingName);
        }

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

        public void register(T mapping, Object handler, Method method) {
            this.readWriteLock.writeLock().lock();
            try {
                // handler method 创建
                HandlerMethod handlerMethod = createHandlerMethod(handler, method);
                // 验证 method mapping
                validateMethodMapping(handlerMethod, mapping);
                // 放入缓存
                this.mappingLookup.put(mapping, handlerMethod);

                // 通过 requestMappingInfo 找到 url
                List<String> directUrls = getDirectUrls(mapping);
                for (String url : directUrls) {
                    this.urlLookup.add(url, mapping);
                }


                String name = ((handler instanceof String) ?
                        (String) handler : handler.getClass().getName())
                        +"#"+method.getName();
                // 设置 handlerMethod + name 的关系
                addMappingName(name, handlerMethod);

                this.registry.put(mapping, new MappingRegistration<>(
                        mapping, handlerMethod, directUrls, name));
            }
            finally {
                this.readWriteLock.writeLock().unlock();
            }
        }

        private void validateMethodMapping(HandlerMethod handlerMethod, T mapping) {
            // Assert that the supplied mapping is unique.
            // 从缓存中获取
            HandlerMethod existingHandlerMethod = this.mappingLookup.get(mapping);
            // 是否为空 , 是否相同
            if (existingHandlerMethod != null && !existingHandlerMethod.equals(handlerMethod)) {
                throw new IllegalStateException("Ambiguous mapping. Cannot map method \n");
            }
        }

        private List<String> getDirectUrls(T mapping) {
            List<String> urls = new ArrayList<>(1);
            urls.add(getMappingPath(mapping));
            return urls;
        }

        private void addMappingName(String name, HandlerMethod handlerMethod) {
            List<HandlerMethod> oldList = this.nameLookup.get(name);
            if (oldList == null) {
                oldList = Collections.emptyList();
            }

            for (HandlerMethod current : oldList) {
                if (handlerMethod.equals(current)) {
                    return;
                }
            }

            List<HandlerMethod> newList = new ArrayList<>(oldList.size() + 1);
            newList.addAll(oldList);
            newList.add(handlerMethod);
            this.nameLookup.put(name, newList);
        }

        public void unregister(T mapping) {
            this.readWriteLock.writeLock().lock();
            try {
                MappingRegistration<T> definition = this.registry.remove(mapping);
                if (definition == null) {
                    return;
                }

                this.mappingLookup.remove(definition.getMapping());

                for (String url : definition.getDirectUrls()) {
                    List<T> list = this.urlLookup.get(url);
                    if (list != null) {
                        list.remove(definition.getMapping());
                        if (list.isEmpty()) {
                            this.urlLookup.remove(url);
                        }
                    }
                }

                removeMappingName(definition);
            }
            finally {
                this.readWriteLock.writeLock().unlock();
            }
        }

        private void removeMappingName(MappingRegistration<T> definition) {
            String name = definition.getMappingName();
            if (name == null) {
                return;
            }
            HandlerMethod handlerMethod = definition.getHandlerMethod();
            List<HandlerMethod> oldList = this.nameLookup.get(name);
            if (oldList == null) {
                return;
            }
            if (oldList.size() <= 1) {
                this.nameLookup.remove(name);
                return;
            }
            List<HandlerMethod> newList = new ArrayList<>(oldList.size() - 1);
            for (HandlerMethod current : oldList) {
                if (!current.equals(handlerMethod)) {
                    newList.add(current);
                }
            }
            this.nameLookup.put(name, newList);
        }
    }


    private class Match {

        private final T mapping;

        private final HandlerMethod handlerMethod;

        public Match(T mapping, HandlerMethod handlerMethod) {
            this.mapping = mapping;
            this.handlerMethod = handlerMethod;
        }

        @Override
        public String toString() {
            return this.mapping.toString();
        }
    }


    private static class MappingRegistration<T> {

        private final T mapping;

        private final HandlerMethod handlerMethod;

        private final List<String> directUrls;


        private final String mappingName;

        public MappingRegistration(T mapping, HandlerMethod handlerMethod,
                                    List<String> directUrls,  String mappingName) {

            Assert.notNull(mapping, "Mapping must not be null");
            Assert.notNull(handlerMethod, "HandlerMethod must not be null");
            this.mapping = mapping;
            this.handlerMethod = handlerMethod;
            this.directUrls = (directUrls != null ? directUrls : Collections.emptyList());
            this.mappingName = mappingName;
        }


        public T getMapping() {
            return this.mapping;
        }

        public HandlerMethod getHandlerMethod() {
            return this.handlerMethod;
        }

        public List<String> getDirectUrls() {
            return this.directUrls;
        }


        public String getMappingName() {
            return this.mappingName;
        }
    }


    private class MatchComparator implements Comparator<Match> {

        private final Comparator<T> comparator;

        public MatchComparator(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public int compare(Match match1, Match match2) {
            return this.comparator.compare(match1.mapping, match2.mapping);
        }
    }
}

