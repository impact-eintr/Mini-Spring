package org.eintr.springframework.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;


public abstract class CollectionUtils {

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     * 判断 collection 是否为空
     *
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty( Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }


    public static <E> void mergeArrayIntoCollection(Object array, Collection<E> collection) {
        Object[] arr = ObjectUtils.toObjectArray(array);
        for (Object elem : arr) {
            collection.add((E) elem);
        }
    }


    public static <K, V> void mergePropertiesIntoMap(Properties props, Map<K, V> map) {
        if (props != null) {
            for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
                String key = (String) en.nextElement();
                Object value = props.get(key);
                if (value == null) {
                    // Allow for defaults fallback or potentially overridden accessor...
                    value = props.getProperty(key);
                }
                map.put((K) key, (V) value);
            }
        }
    }

}