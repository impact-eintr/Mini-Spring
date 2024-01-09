package org.eintr.springframework.web.servlet.handler;

import org.eintr.springframework.util.StringUtils;
import org.eintr.springframework.web.servlet.HandlerExecutionChain;
import org.eintr.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class BeanNameUrlHandlerMapping extends AbstractDetectingUrlHandlerMapping {


    @Override
    protected String[] determineUrlsForHandler(String beanName) {

        // url 列表
        List<String> urls = new ArrayList<>();
        if (beanName.startsWith("/")) {
            urls.add(beanName);
        }
        return StringUtils.toStringArray(urls);
    }
}
