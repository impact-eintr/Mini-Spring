package org.eintr.springframework.web.context.support;

import org.eintr.springframework.context.ApplicationEvent;

public class ServletRequestHandledEvent extends RequestHandledEvent {
    /** URL that triggered the request. */
    private final String requestUrl;

    /** IP address that the request came from. */
    private final String clientAddress;

    /** Usually GET or POST. */
    private final String method;

    /** Name of the servlet that handled the request. */
    private final String servletName;

    /** HTTP status code of the response. */
    private final int statusCode;


    /**
     * Create a new ServletRequestHandledEvent.
     * @param source the component that published the event
     * @param requestUrl the URL of the request
     * @param clientAddress the IP address that the request came from
     * @param method the HTTP method of the request (usually GET or POST)
     * @param servletName the name of the servlet that handled the request
     * @param sessionId the id of the HTTP session, if any
     * @param userName the name of the user that was associated with the
     * request, if any (usually the UserPrincipal)
     * @param processingTimeMillis the processing time of the request in milliseconds
     */
    public ServletRequestHandledEvent(Object source, String requestUrl,
                                      String clientAddress, String method, String servletName,
                                      String sessionId, String userName, long processingTimeMillis) {

        super(source, sessionId, userName, processingTimeMillis);
        this.requestUrl = requestUrl;
        this.clientAddress = clientAddress;
        this.method = method;
        this.servletName = servletName;
        this.statusCode = -1;
    }

    /**
     * Create a new ServletRequestHandledEvent.
     * @param source the component that published the event
     * @param requestUrl the URL of the request
     * @param clientAddress the IP address that the request came from
     * @param method the HTTP method of the request (usually GET or POST)
     * @param servletName the name of the servlet that handled the request
     * @param sessionId the id of the HTTP session, if any
     * @param userName the name of the user that was associated with the
     * request, if any (usually the UserPrincipal)
     * @param processingTimeMillis the processing time of the request in milliseconds
     * @param failureCause the cause of failure, if any
     */
    public ServletRequestHandledEvent(Object source, String requestUrl,
                                      String clientAddress, String method, String servletName,  String sessionId,
                                       String userName, long processingTimeMillis,  Throwable failureCause) {

        super(source, sessionId, userName, processingTimeMillis, failureCause);
        this.requestUrl = requestUrl;
        this.clientAddress = clientAddress;
        this.method = method;
        this.servletName = servletName;
        this.statusCode = -1;
    }

    /**
     * Create a new ServletRequestHandledEvent.
     * @param source the component that published the event
     * @param requestUrl the URL of the request
     * @param clientAddress the IP address that the request came from
     * @param method the HTTP method of the request (usually GET or POST)
     * @param servletName the name of the servlet that handled the request
     * @param sessionId the id of the HTTP session, if any
     * @param userName the name of the user that was associated with the
     * request, if any (usually the UserPrincipal)
     * @param processingTimeMillis the processing time of the request in milliseconds
     * @param failureCause the cause of failure, if any
     * @param statusCode the HTTP status code of the response
     */
    public ServletRequestHandledEvent(Object source, String requestUrl,
                                      String clientAddress, String method, String servletName,  String sessionId,
                                       String userName, long processingTimeMillis,  Throwable failureCause, int statusCode) {

        super(source, sessionId, userName, processingTimeMillis, failureCause);
        this.requestUrl = requestUrl;
        this.clientAddress = clientAddress;
        this.method = method;
        this.servletName = servletName;
        this.statusCode = statusCode;
    }


    /**
     * Return the URL of the request.
     */
    public String getRequestUrl() {
        return this.requestUrl;
    }

    /**
     * Return the IP address that the request came from.
     */
    public String getClientAddress() {
        return this.clientAddress;
    }

    /**
     * Return the HTTP method of the request (usually GET or POST).
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * Return the name of the servlet that handled the request.
     */
    public String getServletName() {
        return this.servletName;
    }

    /**
     * Return the HTTP status code of the response or -1 if the status
     * code is not available.
     * @since 4.1
     */
    public int getStatusCode() {
        return this.statusCode;
    }
}
