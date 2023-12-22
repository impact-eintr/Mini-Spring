package org.eintr.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping{

    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
