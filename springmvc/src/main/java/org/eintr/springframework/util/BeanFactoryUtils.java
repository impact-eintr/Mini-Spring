package org.eintr.springframework.util;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.ListableBeanFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BeanFactoryUtils {

    public static <T> Map<String, T> beansOfTypeIncludingAncestors(
            ListableBeanFactory lbf, Class<T> type)
            throws BeansException {

        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> result = new LinkedHashMap<>(4);
        result.putAll(lbf.getBeansOfType(type));
        return result;
    }


    public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class<?> type) {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type);
        return result;
    }
}
