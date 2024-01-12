package org.eintr.springframework.util;

import org.eintr.springframework.annotation.beans.AliasFor;
import org.eintr.springframework.beans.BeansException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class AnnotationUtils {

    private static Map<SynthesizedAnnotationCacheKey, AttributeValueExtractor> synthesizedCache =
            new ConcurrentHashMap<>(256);

    /**
     * 获取指定元素的直接注解类型
     *
     * @param annotatedElement 可以包含注解的元素，如Class, Field, Method等
     * @param annotationType   注解类型
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement,
                                                         Class<A> annotationType) {
        return annotatedElement.getAnnotation(annotationType);
    }


    /**
     * 获取指定元素的注解类型（若没有直接注解，则从元素其他注解的元注解上查找）
     *
     * @param annotatedElement 可以包含注解的元素，如Field, Method等
     * @param annotationType   注解类型
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A getMergeAnnotation(AnnotatedElement annotatedElement,
                                                              Class<A> annotationType) {
        return getMergeAnnotation(annotatedElement, annotationType, true);
    }

    /**
     * 获取指定元素的注解类型（若没有直接注解，则从元素其他注解的元注解上查找）
     *
     * @param annotatedElement 可以包含注解的元素，如Field, Method等
     * @param annotationType   注解类型
     * @param cacheExtractor   是否缓存属性值提取器
     * @param <A>
     * @return
     */
    private static <A extends Annotation> A getMergeAnnotation(AnnotatedElement annotatedElement,
                                                               Class<A> annotationType,
                                                               boolean cacheExtractor) {
        /*
        1. @GetMapping
            public void getUser() {
                // do sth ...
            }

         2. 判断getUser上有没有 @RequestMapping
         3. 没有，那么遍历 getUser 上的所有注解
         4. 找到 @GetMapping 通过递归尝试认定这是 @RequestMapping 的组合注解
         */

        A annotation = annotatedElement.getAnnotation(annotationType);
        // 如果元素中含有直接注解
        if (annotation != null) {
            return annotation;
        }

        List<Annotation> visited = new ArrayList<>();
        for (Annotation otherAnn : annotatedElement.getAnnotations()) {
            if ((annotation = findMetaAnnotation(otherAnn, annotationType, visited)) == null) {
                visited.clear();
                continue;
            }
            break;
        }

        if (annotation == null) { // 没有直接return
            return null;
        }

        // 属性值提取 来构造这个继承关系
        AttributeValueExtractor valueExtractor;
        if (cacheExtractor) {
            // 组合注解缓存key
            SynthesizedAnnotationCacheKey key =
                    SynthesizedAnnotationCacheKey.of(annotatedElement, annotationType);
            valueExtractor = synthesizedCache.get(key);
            // 生成对应的组合注解属性值提取器，并缓存
            if (valueExtractor == null) {
                valueExtractor = DefaultAttributeExtractor.from(annotatedElement, annotation, visited);
                synthesizedCache.put(key, valueExtractor);
            }
        } else {
            valueExtractor = DefaultAttributeExtractor.from(annotatedElement, annotation, visited);
        }
        return synthesizeAnnotation(annotatedElement, annotation, valueExtractor);
    }


    private static <A extends Annotation> A findMetaAnnotation(Annotation annotation,
                                                               Class<A> targetType,
                                                               List<Annotation> visited) {
        visited.add(annotation);
        A a = annotation.annotationType().getAnnotation(targetType);
        if (a == null) {
            for (Annotation metaAnn : annotation.annotationType().getAnnotations()) {
                Class<? extends Annotation> metaAnnType = metaAnn.annotationType();
                // 如果是java自带的元注解，则跳过继续
                if (metaAnnType.getName().startsWith("java.lang.annotation")) {
                    continue;
                }
                if ((a = metaAnnType.getAnnotation(targetType)) != null) {
                    visited.add(metaAnn);
                    break;
                }
                if ((a = findMetaAnnotation(metaAnn, targetType, visited)) != null) {
                    break;
                }
                visited.remove(visited.size()-1);
            }
        }
        return a;
    }


    /**
     * 将注解使用动态代理包装，以实现组合注解功能
     *
     * @param annotatedElement 注解作用的元素
     * @param valueExtractor   属性值提取器
     * @param <A>
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <A extends Annotation> A synthesizeAnnotation(AnnotatedElement annotatedElement,
                                                                 Annotation annotation,
                                                                 AttributeValueExtractor valueExtractor) {
        // 组合注解代理
        return (A) Proxy.newProxyInstance(annotation.getClass().getClassLoader()
                , new Class<?>[]{annotation.annotationType()}
                , new SynthesizedAnnotationInvocationHandler(valueExtractor));
    }

    /**
     * 判断指定元素是否含有某个注解（若没有直接注解，则从元素其他注解的元注解上查找）
     *
     * @param annotatedElement 可以包含注解的元素，如Field, Method等
     * @param annotationType   注解类型
     * @param <A>
     * @return
     */
    public static <A extends Annotation> boolean isAnnotationPresent(AnnotatedElement annotatedElement, Class<A> annotationType) {
        return getMergeAnnotation(annotatedElement, annotationType) != null;
    }



    /**
     * 获取注解的属性值
     *
     * @param annotation    注解对象
     * @param attributeName 属性名（即方法名）
     * @return 属性值
     */
    public static Object getAttributeValue(Annotation annotation, String attributeName) {
        if (annotation == null || StringUtils.isEmpty(attributeName)) {
            return null;
        }
        try {
            return ReflectionUtils.invokeMethod(
                    annotation.annotationType().getDeclaredMethod(attributeName),
                    annotation);
        } catch (Exception ex) {
            String msg = String.format("%s注解无法获取%s属性值！", annotation.annotationType().getName(), attributeName);
            throw new IllegalArgumentException(msg, ex);
        }
    }


    /**
     * 获取注解的属性以及属性值；key->属性  value->属性值
     *
     * @param annotation 注解类
     * @return Map
     */
    public static Map<String, Object> getAttributeMap(Annotation annotation) {
        Map<String, Object> attributeMap = new HashMap<>(8);
        getAttributeMethods(annotation.getClass()).forEach(method -> {
            attributeMap.put(method.getName(), ReflectionUtils.invokeMethod(method, annotation));
        });
        return attributeMap;
    }


    /**
     * 获取注解中的所有属性方法
     *
     * @param annotationType 注解类型
     * @return 属性方法列表
     */
    private static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
        List<Method> methods = new ArrayList<>();
        for (Method method : annotationType.getDeclaredMethods()) {
            if (isAttributeMethod(method)) {
                methods.add(method);
            }
        }
        return methods;
    }

    /**
     * 判断方法是否为注解的属性方法
     *
     * @param method 方法
     * @return
     */
    private static boolean isAttributeMethod(Method method) {
        return (method != null && method.getParameterCount() == 0 && method.getReturnType() != void.class);
    }

    /**
     * 属性值获取器
     */
    interface AttributeValueExtractor {
        /**
         * 根据属性名获取对应的属性值
         *
         * @param attribute 属性名（注解方法名）
         * @return 属性值
         */
        Object getAttributeValue(String attribute);

        /**
         * 获取注解
         *
         * @return 目标注解
         */
        Annotation getAnnotation();
    }

    private static class DefaultAttributeExtractor implements AttributeValueExtractor {

        /**
         * 目标注解 @GetMapping
         */
        private Annotation annotation;

        /**
         * 注解属性 key:属性名（即注解中的方法名） value:属性值
         */
        private Map<String, Object> attributes;

        @Override
        public Object getAttributeValue(String attribute) {
            return attributes.get(attribute);
        }

        @Override
        public Annotation getAnnotation() {
            return annotation;
        }

        private DefaultAttributeExtractor() {
        }

        private DefaultAttributeExtractor(Annotation annotation, Map<String, Object> attributes) {
            this.annotation = annotation;
            this.attributes = attributes;
        }

        private static DefaultAttributeExtractor from(AnnotatedElement element,
                                                      Annotation target,
                                                      List<Annotation> visited) {
            Class<? extends Annotation> targetType = target.annotationType();
            Map<String, Object> attributes = new LinkedHashMap<>(8);
            for (Annotation a : visited) {
                List<AliasDescriptor> aliasDescriptors = getAliasDescriptors(a);
                if (aliasDescriptors.isEmpty()) {
                    continue;
                }
                // TODO 需要判断是否需要覆盖  比如 @GetMapping.value()
                // 遍历该注解上的所有别名描述器，并判断目标注解的属性是否有被其他注解覆盖
                for (AliasDescriptor descriptor : aliasDescriptors) {
                    if (descriptor.aliasAnnotationType == targetType) {
                        String targetAttributeName = descriptor.aliasAttributeName;
                        if (!attributes.containsKey(targetAttributeName)) {
                            attributes.put(targetAttributeName,
                                    ReflectionUtils.invokeMethod(
                                            descriptor.sourceAttribute,
                                            AnnotationUtils.getMergeAnnotation(
                                                    element,
                                                    descriptor.sourceAnnotationType,
                                                    false)));
                        }
                    }
                }
            }

            // 判断那没有覆盖 没有覆盖的需要填补 比如 @RequestMapping.method()
            for (Method targetAttribute : getAttributeMethods(targetType)) {
                String attributeName = targetAttribute.getName();
                if (!attributes.containsKey(attributeName)) {
                    attributes.put(attributeName,
                            ReflectionUtils.invokeMethod(targetAttribute, target));
                }
            }
            return new DefaultAttributeExtractor(target, attributes);
        }
    }


    private static List<AliasDescriptor> getAliasDescriptors(Annotation a) {
        List<AliasDescriptor> descriptors = null;
        for (Method attribute : getAttributeMethods(a.annotationType())) {
            AliasDescriptor des = AliasDescriptor.from(attribute);
            if (des != null) {
                if (descriptors == null) {
                    descriptors = new ArrayList<>(8);
                }
                descriptors.add(des);
            }
        }
        return Optional.ofNullable(descriptors).orElse(Collections.emptyList());
    }

    /**
     * 组合注解代理处理器
     */
    static public class SynthesizedAnnotationInvocationHandler implements InvocationHandler {
        private final AttributeValueExtractor attributeValueExtractor;

        SynthesizedAnnotationInvocationHandler(AttributeValueExtractor valueExtractor) {
            this.attributeValueExtractor = valueExtractor;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return this.attributeValueExtractor.getAttributeValue(method.getName());
        }
    }


    public static class AliasDescriptor {

        /**
         * 源注解属性方法
         */
        private final Method sourceAttribute;

        /**
         * 源注解类型
         */
        private final Class<? extends Annotation> sourceAnnotationType;

        /**
         * 源注解属性名（即方法名）
         */
        private final String sourceAttributeName;


        /**
         * 别名的注解属性方法
         */
        private final Method aliasAttribute;

        /**
         * 别名的注解类型
         */
        private final Class<? extends Annotation> aliasAnnotationType;

        /**
         * 别名的属性名
         */
        private final String aliasAttributeName;

        public static AliasDescriptor from(Method attribute) {
            AliasFor aliasFor = attribute.getAnnotation(AliasFor.class);
            if (aliasFor == null) {
                return null;
            }

            return new AliasDescriptor(attribute, aliasFor);
        }

        @SuppressWarnings("unchecked")
        private AliasDescriptor(Method sourceAttribute, AliasFor aliasFor) {

            Class<?> declaringClass = sourceAttribute.getDeclaringClass();
            this.sourceAttribute = sourceAttribute;
            this.sourceAnnotationType = (Class<? extends Annotation>) declaringClass;
            this.sourceAttributeName = sourceAttribute.getName();
            this.aliasAnnotationType = aliasFor.annotation();
            this.aliasAttributeName = getAliasAttributeName(aliasFor, sourceAttribute);
            try {
                this.aliasAttribute = this.aliasAnnotationType.getDeclaredMethod(this.aliasAttributeName);
            } catch (NoSuchMethodException ex) {
                String msg = String.format(
                        "@%s注解中%s属性方法上@OverrideFor注解对应的%s别名属性不存在于%s别名注解类中！",
                        this.sourceAnnotationType.getName(), this.sourceAttributeName,
                        this.aliasAttributeName, this.aliasAnnotationType.getName());
                throw new BeansException(msg, ex);
            }
        }


        private String getAliasAttributeName(AliasFor aliasFor, Method sourceAttribute) {
            String attribute = aliasFor.attribute();
            String value = aliasFor.value();
            boolean hasAttribute = !StringUtils.isEmpty(attribute);
            boolean hasValue = !StringUtils.isEmpty(value);
            if (hasAttribute && hasValue) {
                String msg = String.format("%s注解中的%s属性方法上的@AliasFor注解的value属性和attribute不能同时存在！",
                        sourceAttribute.getDeclaringClass().getName(), sourceAttribute.getName());
                throw new BeansException(msg);
            }

            attribute = hasAttribute ? attribute : value;
            return !StringUtils.isEmpty(attribute) ? attribute : sourceAttribute.getName();
        }
    }

    /**
     * 组合注解缓存key
     */
    private static class SynthesizedAnnotationCacheKey {
        /**
         * 注解作用元素
         */
        private AnnotatedElement element;

        /**
         * 目标注解类
         */
        private Class<? extends Annotation> targetAnnotation;

        private SynthesizedAnnotationCacheKey(AnnotatedElement element, Class<? extends Annotation> targetAnnotation) {
            this.element = element;
            this.targetAnnotation = targetAnnotation;
        }

        private static SynthesizedAnnotationCacheKey of(AnnotatedElement element, Class<? extends Annotation> target) {
            return new SynthesizedAnnotationCacheKey(element, target);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof SynthesizedAnnotationCacheKey)) {
                return false;
            }
            SynthesizedAnnotationCacheKey otherKey = (SynthesizedAnnotationCacheKey) other;
            return (this.element.equals(otherKey.element) && this.targetAnnotation.equals(otherKey.targetAnnotation));
        }

        @Override
        public int hashCode() {
            return (this.element.hashCode() * 29 + this.targetAnnotation.hashCode());
        }
    }
}
