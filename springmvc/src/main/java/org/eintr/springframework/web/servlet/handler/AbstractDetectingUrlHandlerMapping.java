package org.eintr.springframework.web.servlet.handler;

import org.eintr.springframework.beans.BeansException;
import org.eintr.springframework.context.ApplicationContext;
import org.eintr.springframework.util.BeanFactoryUtils;
import org.eintr.springframework.util.BeanUtils;
import org.eintr.springframework.util.ObjectUtils;


public abstract class AbstractDetectingUrlHandlerMapping extends AbstractUrlHandlerMapping {

    private boolean detectHandlersInAncestorContexts = false;

    public void initApplicationContext() throws BeansException {
        super.initApplicationContext();
        detectHandlers();
    }

    protected void detectHandlers() throws BeansException {
        // 提取上下文
        ApplicationContext applicationContext = getApplicationContext();
        // 获取所有所有的BeanName
        String[] beanNames = (this.detectHandlersInAncestorContexts ?
                BeanFactoryUtils.beanNamesForTypeIncludingAncestors(applicationContext, Object.class) :
                applicationContext.getBeanNamesForType(Object.class));

        // Take any bean name that we can determine URLs for.
        // 循环处理BeanName 找到以/开头的数据
        for (String beanName : beanNames) {
            String[] urls = determineUrlsForHandler(beanName);
            if (!ObjectUtils.isEmpty(urls)) {
                // URL paths found: Let's consider it a handler.
                // 注册处理器
                registerHandler(urls, beanName);
            }
        }
    }

    protected abstract String[] determineUrlsForHandler(String beanName);
}
