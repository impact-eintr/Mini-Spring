package org.eintr.springframework.util;

import java.util.Collection;


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
}