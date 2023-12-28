package org.eintr.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping{

    String LOOKUP_PATH = HandlerMapping.class.getName() + ".lookupPath";

    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
