package org.eintr.springframework.util;

import org.eintr.springframework.http.HttpHeaders;
import org.eintr.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;


public abstract class CorsUtils {

    /**
     * Returns {@code true} if the request is a valid CORS one by checking {@code Origin}
     * header presence and ensuring that origins are different.
     */
    //public static boolean isCorsRequest(HttpServletRequest request) {
    //    String origin = request.getHeader(HttpHeaders.ORIGIN);
    //    if (origin == null) {
    //        return false;
    //    }
    //    UriComponents originUrl = UriComponentsBuilder.fromOriginHeader(origin).build();
    //    String scheme = request.getScheme();
    //    String host = request.getServerName();
    //    int port = request.getServerPort();
    //    return !(ObjectUtils.nullSafeEquals(scheme, originUrl.getScheme()) &&
    //            ObjectUtils.nullSafeEquals(host, originUrl.getHost()) &&
    //            getPort(scheme, port) == getPort(originUrl.getScheme(), originUrl.getPort()));

    //}

    private static int getPort(String scheme, int port) {
        if (port == -1) {
            if ("http".equals(scheme) || "ws".equals(scheme)) {
                port = 80;
            }
            else if ("https".equals(scheme) || "wss".equals(scheme)) {
                port = 443;
            }
        }
        return port;
    }

    /**
     * Returns {@code true} if the request is a valid CORS pre-flight one by checking {code OPTIONS} method with
     * {@code Origin} and {@code Access-Control-Request-Method} headers presence.
     */
    public static boolean isPreFlightRequest(HttpServletRequest request) {
        return (HttpMethod.OPTIONS.matches(request.getMethod()) &&
                request.getHeader(HttpHeaders.ORIGIN) != null &&
                request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null);
    }

}
