package org.eintr.springframework.util;

import java.util.List;
import java.util.Map;

public interface MultiValueMap<K, V> extends Map<K, List<V>> {

    /**
     * Return the first value for the given key.
     * 获取value的第一个
     * @param key the key
     * @return the first value for the specified key, or {@code null} if none
     */

    V getFirst(K key);

    /**
     * Add the given single value to the current list of values for the given key.
     * 添加元素
     * @param key the key
     * @param value the value to be added
     */
    void add(K key,  V value);

    /**
     * Add all the values of the given list to the current list of values for the given key.
     * 添加所有元素
     * @param key they key
     * @param values the values to be added
     * @since 5.0
     */
    void addAll(K key, List<? extends V> values);

    /**
     * Add all the values of the given {@code MultiValueMap} to the current values.
     *
     * 添加要给 {@link MultiValueMap} 对象
     * @param values the values to be added
     * @since 5.0
     */
    void addAll(MultiValueMap<K, V> values);

    /**
     * {@link #add(Object, Object) Add} the given value, only when the map does not
     * {@link #containsKey(Object) contain} the given key.
     * @param key the key
     * @param value the value to be added
     * @since 5.2
     */
    default void addIfAbsent(K key,  V value) {
        if (!containsKey(key)) {
            add(key, value);
        }
    }

    /**
     * Set the given single value under the given key.
     *
     * 设置数据
     * @param key the key
     * @param value the value to set
     */
    void set(K key,  V value);

    /**
     * Set the given values under.
     * 设置一个map数据
     * @param values the values.
     */
    void setAll(Map<K, V> values);

    /**
     * Return a {@code Map} with the first values contained in this {@code MultiValueMap}.
     * 转换成 map 结构
     * @return a single value representation of this map
     */
    Map<K, V> toSingleValueMap();

}
