package org.eintr.springframework.util;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.beans.factory.HierarchicalBeanFactory;
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
        //if (lbf instanceof HierarchicalBeanFactory) {
        //    HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
        //    if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
        //        Map<String, T> parentResult = beansOfTypeIncludingAncestors(
        //                (ListableBeanFactory) hbf.getParentBeanFactory(), type, //includeNonSingletons, allowEagerInit);
        //        parentResult.forEach((beanName, beanInstance) -> {
        //            if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
        //                result.put(beanName, beanInstance);
        //            }
        //        });
        //    }
        //}
        return result;
    }
}
