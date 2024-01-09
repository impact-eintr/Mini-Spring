package org.eintr.springframework.web.servlet.mvc.method.annotation;

import org.eintr.springframework.annotation.mvc.RequestMapping;
import org.eintr.springframework.annotation.stereotype.Controller;
import org.eintr.springframework.web.method.RequestMappingInfo;
import org.eintr.springframework.web.servlet.HandlerMapping;
import org.eintr.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.eintr.springframework.web.servlet.handler.MatchableHandlerMapping;
import org.eintr.springframework.web.servlet.handler.RequestMatchResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class RequestMappingHandlerMethodMapping
        extends AbstractHandlerMethodMapping<RequestMappingInfo>
        implements MatchableHandlerMapping {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return (beanType.isAnnotationPresent(Controller.class)) ||
            (beanType.isAnnotationPresent(RequestMapping.class));
    }

    @Override
    protected String getMappingPath(RequestMappingInfo mapping) {
        mapping.getRequestMethod();
        return mapping.getRequestPath();
    }

    @Override
    protected RequestMappingInfo getMatchingMapping(RequestMappingInfo mapping, HttpServletRequest request) {
        return mapping.getMatchingCondition(request);
    }

    @Override
    protected Comparator<RequestMappingInfo> getMappingComparator(final HttpServletRequest request) {
        return (info1, info2) -> info1.compareTo(info2, request);
    }


    @Override
    public RequestMatchResult match(HttpServletRequest request, String pattern) {
        return null;
    }

    protected void handleMatch(RequestMappingInfo info, String lookupPath, HttpServletRequest request) {
        super.handleMatch(info, lookupPath, request);

        String bestPattern;
        bestPattern = lookupPath;
        request.setAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE, bestPattern);
    }
}
