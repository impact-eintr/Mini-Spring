package org.eintr.springframework.web.util;

import javax.servlet.http.HttpServletRequest;

public class UrlPathHelper {

    /**
     * 是否全路径标记
     */
    private boolean alwaysUseFullPath = false;

    public String getLookupPathForRequest(HttpServletRequest request) {
        // 是否全路径
        if (this.alwaysUseFullPath) {
            // 获取带有application的地址
            //return getPathWithinApplication(request);
        }
        // Else, use path within current servlet mapping if applicable
        // 获取带有servlet 地址的路由
        String rest = getPathWithinServletMapping(request);
        if (!"".equals(rest)) {
            return rest;
        }
        else {
            // 获取带有application的地址
            return getPathWithinApplication(request);
        }
    }


    public String getPathWithinApplication(HttpServletRequest request) {
        // 获取 context path
        String contextPath = getContextPath(request);
        // 获取 uri
        String requestUri = getRequestUri(request);
        // 剩余路径
        String path = getRemainingPath(requestUri, contextPath, true);
        if (path != null) {
            // Normal case: URI contains context path.
            return (StringUtils.hasText(path) ? path : "/");
        }
        else {
            return requestUri;
        }
    }
}
