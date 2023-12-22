package org.eintr.springframework.web.servlet.handler;

import org.eintr.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

public interface MatchableHandlerMapping extends HandlerMapping {
    RequestMatchResult match(HttpServletRequest request, String pattern);
}
