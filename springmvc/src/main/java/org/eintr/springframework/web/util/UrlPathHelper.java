package org.eintr.springframework.web.util;

import org.eintr.springframework.util.StringUtils;
import org.eintr.springframework.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Properties;

public class UrlPathHelper {

    private static final String WEBSPHERE_URI_ATTRIBUTE = "com.ibm.websphere.servlet.uri_non_decoded";

    static volatile Boolean websphereComplianceFlag;
    /**
     * 是否全路径标记
     */
    private boolean alwaysUseFullPath = false;

    /**
     * 是否需要 decode
     */
    private boolean urlDecode = true;


    private String defaultEncoding = WebUtils.DEFAULT_CHARACTER_ENCODING;


    protected String getDefaultEncoding() {
        return this.defaultEncoding;
    }

    public String getLookupPathForRequest(HttpServletRequest request) {
        // 是否全路径
        if (this.alwaysUseFullPath) {
            // 获取带有application的地址
            return getPathWithinApplication(request);
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

    public String getPathWithinServletMapping(HttpServletRequest request) {
        // 获取 context-path + uri
        String pathWithinApp = getPathWithinApplication(request);
        // 获取 servlet path
        String servletPath = getServletPath(request);
        // 去掉双斜杠
        String sanitizedPathWithinApp = getSanitizedPath(pathWithinApp);

        String path = "";

        if (servletPath.contains(sanitizedPathWithinApp)) {
            path = getRemainingPath(sanitizedPathWithinApp, servletPath, false);
        } else {
            path = getRemainingPath(pathWithinApp, servletPath, false);
        }

        return path;
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

    public String getServletPath(HttpServletRequest request) {
        String servletPath = (String) request.getAttribute(WebUtils.INCLUDE_SERVLET_PATH_ATTRIBUTE);
        if (servletPath == null) {
            servletPath = request.getServletPath();
        }
        if (servletPath.length() > 1 && servletPath.endsWith("/") &&
                shouldRemoveTrailingServletPathSlash(request)) {
            // On WebSphere, in non-compliant mode, for a "/foo/" case that would be "/foo"
            // on all other servlet containers: removing trailing slash, proceeding with
            // that remaining slash as final lookup path...
            servletPath = servletPath.substring(0, servletPath.length() - 1);
        }
        return servletPath;
    }


    public String getContextPath(HttpServletRequest request) {
        // 从 request 获取 context path
        String contextPath = (String) request.getAttribute(WebUtils.INCLUDE_CONTEXT_PATH_ATTRIBUTE);
        if (contextPath == null) {
            contextPath = request.getContextPath();
        }
        if ("/".equals(contextPath)) {
            // Invalid case, but happens for includes on Jetty: silently adapt it.
            contextPath = "";
        }
        // decode context path
        return decodeRequestString(request, contextPath);
    }


    public String getRequestUri(HttpServletRequest request) {
        // 从属性中获取
        String uri = (String) request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE);
        if (uri == null) {
            // 调用方法获取
            uri = request.getRequestURI();
        }
        //编码和清理数据
        return decodeAndCleanUriString(request, uri);
    }

    private String getRemainingPath(String requestUri, String mapping, boolean ignoreCase) {
        int index1 = 0;
        int index2 = 0;
        for (; (index1 < requestUri.length()) && (index2 < mapping.length()); index1++, index2++) {
            char c1 = requestUri.charAt(index1);
            char c2 = mapping.charAt(index2);
            if (c1 == ';') {
                index1 = requestUri.indexOf('/', index1);
                if (index1 == -1) {
                    return null;
                }
                c1 = requestUri.charAt(index1);
            }
            if (c1 == c2 || (ignoreCase && (Character.toLowerCase(c1) == Character.toLowerCase(c2)))) {
                continue;
            }
            return null;
        }
        if (index2 != mapping.length()) {
            return null;
        }
        else if (index1 == requestUri.length()) {
            return "";
        }
        else if (requestUri.charAt(index1) == ';') {
            index1 = requestUri.indexOf('/', index1);
        }
        return (index1 != -1 ? requestUri.substring(index1) : "");
    }


    private String decodeAndCleanUriString(HttpServletRequest request, String uri) {
        // 去掉分号
        uri = removeSemicolonContent(uri);
        // decoding
        uri = decodeRequestString(request, uri);
        // 去掉 // 双斜杠
        uri = getSanitizedPath(uri);
        return uri;
    }

    public String decodeRequestString(HttpServletRequest request, String source) {
        // 判断是否需要编码
        if (this.urlDecode) {
            // 进行编码
            return decodeInternal(request, source);
        }
        return source;
    }


    public String removeSemicolonContent(String requestUri) {
        return removeSemicolonContentInternal(requestUri) ;
    }


    private String removeSemicolonContentInternal(String requestUri) {
        int semicolonIndex = requestUri.indexOf(';');
        while (semicolonIndex != -1) {
            int slashIndex = requestUri.indexOf('/', semicolonIndex);
            String start = requestUri.substring(0, semicolonIndex);
            requestUri = (slashIndex != -1) ? start + requestUri.substring(slashIndex) : start;
            semicolonIndex = requestUri.indexOf(';', semicolonIndex);
        }
        return requestUri;
    }


    private String getSanitizedPath(final String path) {
        String sanitized = path;
        while (true) {
            int index = sanitized.indexOf("//");
            if (index < 0) {
                break;
            }
            else {
                sanitized = sanitized.substring(0, index) +
                        sanitized.substring(index + 1);
            }
        }
        return sanitized;
    }

    @SuppressWarnings("deprecation")
    private String decodeInternal(HttpServletRequest request, String source) {
        // 确定编码方式
        String enc = determineEncoding(request);
        try {
            // 将 source 编译成 enc 的编码方式
            return StringUtils.uriDecode(source, Charset.forName(enc));
        }
        catch (UnsupportedCharsetException ex) {
            // 直接编码,JDK底层编码
            return URLDecoder.decode(source);
        }
    }


    protected String determineEncoding(HttpServletRequest request) {
        // 从 request 中获取编码方式
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            // 默认编码
            enc = getDefaultEncoding();
        }
        return enc;
    }

    /**
     * 是否删除 servlet path 后面的斜杠
     * @param request
     * @return
     */
    private boolean shouldRemoveTrailingServletPathSlash(HttpServletRequest request) {
        if (request.getAttribute(WEBSPHERE_URI_ATTRIBUTE) == null) {
            // Regular servlet container: behaves as expected in any case,
            // so the trailing slash is the result of a "/" url-pattern mapping.
            // Don't remove that slash.
            return false;
        }
        Boolean flagToUse = websphereComplianceFlag;
        if (flagToUse == null) {
            ClassLoader classLoader = UrlPathHelper.class.getClassLoader();
            String className = "com.ibm.ws.webcontainer.WebContainer";
            String methodName = "getWebContainerProperties";
            String propName = "com.ibm.ws.webcontainer.removetrailingservletpathslash";
            boolean flag = false;
            try {
                Class<?> cl = classLoader.loadClass(className);
                Properties prop = (Properties) cl.getMethod(methodName).invoke(null);
                flag = Boolean.parseBoolean(prop.getProperty(propName));
            }
            catch (Throwable ex) {

            }
            flagToUse = flag;
            websphereComplianceFlag = flag;
        }
        // Don't bother if WebSphere is configured to be fully Servlet compliant.
        // However, if it is not compliant, do remove the improper trailing slash!
        return !flagToUse;
    }

}
