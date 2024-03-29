package org.eintr.springframework.web.servlet;

import org.eintr.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(Object handler);


    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;


    long getLastModified(HttpServletRequest request, Object handler);

}
