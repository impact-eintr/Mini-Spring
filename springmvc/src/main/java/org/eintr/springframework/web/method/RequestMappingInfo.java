package org.eintr.springframework.web.method;

import org.eintr.springframework.annotation.mvc.RequestMethod;
import org.eintr.springframework.web.servlet.HandlerMapping;
import org.eintr.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public final class RequestMappingInfo {
    //请求的uri
    private String requestPath;

    //请求的类型，GET、POST....
    private RequestMethod requestMethod;

    private final Set<RequestMethod> methods;

    private UrlPathHelper helper = new UrlPathHelper();

    public RequestMappingInfo(String requestPath, RequestMethod... requestMethods) {
        if (!"/".equals(requestPath) && requestPath.endsWith("/")) {
            requestPath = requestPath.substring(0, requestPath.lastIndexOf("/"));
        }
        this.requestPath = requestPath;
        this.methods =  Collections.unmodifiableSet(new LinkedHashSet<>(
                Arrays.asList(requestMethods)));
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }


    public RequestMappingInfo getMatchingCondition(HttpServletRequest request) {
        if (!matchPath(request)) {
            return null;
        }
        if (!matchMethod(request)) {
            return null;
        }
        return this;
    }


    public boolean matchPath(HttpServletRequest request) {
        String path = helper.getLookupPathForRequest(request, HandlerMapping.LOOKUP_PATH);
        return this.requestPath.equals(path);
    }

    public boolean matchMethod(HttpServletRequest request) {
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        return this.requestMethod.equals(method);
    }

    public int compareTo(RequestMappingInfo other, HttpServletRequest request) {
        int result;
        if ((result = comparePathTo(other, request)) != 0) {
            return result;
        }
        if ((result = compareMethodTo(other, request)) != 0) {
            return result;
        }
        return 0;
    }

    public int compareMethodTo(RequestMappingInfo other, HttpServletRequest request) {
        if (other.methods.size() != this.methods.size()) {
            return other.methods.size() - this.methods.size();
        }
        else if (this.methods.size() == 1) {
            if (this.methods.contains(RequestMethod.HEAD) && other.methods.contains(RequestMethod.GET)) {
                return -1;
            }
            else if (this.methods.contains(RequestMethod.GET) && other.methods.contains(RequestMethod.HEAD)) {
                return 1;
            }
        }
        return 0;
    }

    public int comparePathTo(RequestMappingInfo other, HttpServletRequest request) {
        return other.requestPath.compareTo(this.requestPath);
    }

}
