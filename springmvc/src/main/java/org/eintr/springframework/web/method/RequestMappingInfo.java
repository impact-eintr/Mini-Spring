package org.eintr.springframework.web.method;

public final class RequestMappingInfo {
    //请求的uri
    private String requestPath;

    //请求的类型，GET、POST....
    private String requestMethod;

    public RequestMappingInfo(String requestPath, String requestMethod) {
        if (!"/".equals(requestPath) && requestPath.endsWith("/")) {
            requestPath = requestPath.substring(0, requestPath.lastIndexOf("/"));
        }
        this.requestPath = requestPath;
        this.requestMethod = requestMethod;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }
}
