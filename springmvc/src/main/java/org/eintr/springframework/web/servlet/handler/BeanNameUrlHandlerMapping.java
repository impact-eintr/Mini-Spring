package org.eintr.springframework.web.servlet.handler;

import org.eintr.springframework.web.servlet.HandlerExecutionChain;
import org.eintr.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

public class BeanNameUrlHandlerMapping implements HandlerMapping {

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        return null;
    }
}
