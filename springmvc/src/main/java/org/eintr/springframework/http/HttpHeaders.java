package org.eintr.springframework.http;

import cn.hutool.core.lang.Assert;
import org.eintr.springframework.util.LinkedMultiValueMap;
import org.eintr.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class HttpHeaders  implements MultiValueMap<String, String>, Serializable {


    private static final long serialVersionUID = -8578554704772377436L;


    /**
     * The HTTP {@code Accept} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.2">Section 5.3.2 of RFC 7231</a>
     */
    public static final String ACCEPT = "Accept";
    /**
     * The HTTP {@code Accept-Charset} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.3">Section 5.3.3 of RFC 7231</a>
     */
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    /**
     * The HTTP {@code Accept-Encoding} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.4">Section 5.3.4 of RFC 7231</a>
     */
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    /**
     * The HTTP {@code Accept-Language} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.5">Section 5.3.5 of RFC 7231</a>
     */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    /**
     * The HTTP {@code Accept-Ranges} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-2.3">Section 5.3.5 of RFC 7233</a>
     */
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    /**
     * The CORS {@code Access-Control-Allow-Credentials} response header field name.
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    /**
     * The CORS {@code Access-Control-Allow-Headers} response header field name.
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    /**
     * The CORS {@code Access-Control-Allow-Methods} response header field name.
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    /**
     * The CORS {@code Access-Control-Allow-Origin} response header field name.
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    /**
     * The CORS {@code Access-Control-Expose-Headers} response header field name.
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    /**
     * The CORS {@code Access-Control-Max-Age} response header field name.
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    /**
     * The CORS {@code Access-Control-Request-Headers} request header field name.
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    /**
     * The CORS {@code Access-Control-Request-Method} request header field name.
     * @see <a href="https://www.w3.org/TR/cors/">CORS W3C recommendation</a>
     */
    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    /**
     * The HTTP {@code Age} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.1">Section 5.1 of RFC 7234</a>
     */
    public static final String AGE = "Age";
    /**
     * The HTTP {@code Allow} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.4.1">Section 7.4.1 of RFC 7231</a>
     */
    public static final String ALLOW = "Allow";
    /**
     * The HTTP {@code Authorization} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-4.2">Section 4.2 of RFC 7235</a>
     */
    public static final String AUTHORIZATION = "Authorization";
    /**
     * The HTTP {@code Cache-Control} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.2">Section 5.2 of RFC 7234</a>
     */
    public static final String CACHE_CONTROL = "Cache-Control";
    /**
     * The HTTP {@code Connection} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-6.1">Section 6.1 of RFC 7230</a>
     */
    public static final String CONNECTION = "Connection";
    /**
     * The HTTP {@code Content-Encoding} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.2.2">Section 3.1.2.2 of RFC 7231</a>
     */
    public static final String CONTENT_ENCODING = "Content-Encoding";
    /**
     * The HTTP {@code Content-Disposition} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc6266">RFC 6266</a>
     */
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    /**
     * The HTTP {@code Content-Language} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.3.2">Section 3.1.3.2 of RFC 7231</a>
     */
    public static final String CONTENT_LANGUAGE = "Content-Language";
    /**
     * The HTTP {@code Content-Length} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-3.3.2">Section 3.3.2 of RFC 7230</a>
     */
    public static final String CONTENT_LENGTH = "Content-Length";
    /**
     * The HTTP {@code Content-Location} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.4.2">Section 3.1.4.2 of RFC 7231</a>
     */
    public static final String CONTENT_LOCATION = "Content-Location";
    /**
     * The HTTP {@code Content-Range} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-4.2">Section 4.2 of RFC 7233</a>
     */
    public static final String CONTENT_RANGE = "Content-Range";
    /**
     * The HTTP {@code Content-Type} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-3.1.1.5">Section 3.1.1.5 of RFC 7231</a>
     */
    public static final String CONTENT_TYPE = "Content-Type";
    /**
     * The HTTP {@code Cookie} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc2109#section-4.3.4">Section 4.3.4 of RFC 2109</a>
     */
    public static final String COOKIE = "Cookie";
    /**
     * The HTTP {@code Date} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.1.2">Section 7.1.1.2 of RFC 7231</a>
     */
    public static final String DATE = "Date";
    /**
     * The HTTP {@code ETag} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.3">Section 2.3 of RFC 7232</a>
     */
    public static final String ETAG = "ETag";
    /**
     * The HTTP {@code Expect} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.1.1">Section 5.1.1 of RFC 7231</a>
     */
    public static final String EXPECT = "Expect";
    /**
     * The HTTP {@code Expires} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.3">Section 5.3 of RFC 7234</a>
     */
    public static final String EXPIRES = "Expires";
    /**
     * The HTTP {@code From} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.1">Section 5.5.1 of RFC 7231</a>
     */
    public static final String FROM = "From";
    /**
     * The HTTP {@code Host} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-5.4">Section 5.4 of RFC 7230</a>
     */
    public static final String HOST = "Host";
    /**
     * The HTTP {@code If-Match} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.1">Section 3.1 of RFC 7232</a>
     */
    public static final String IF_MATCH = "If-Match";
    /**
     * The HTTP {@code If-Modified-Since} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.3">Section 3.3 of RFC 7232</a>
     */
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    /**
     * The HTTP {@code If-None-Match} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.2">Section 3.2 of RFC 7232</a>
     */
    public static final String IF_NONE_MATCH = "If-None-Match";
    /**
     * The HTTP {@code If-Range} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-3.2">Section 3.2 of RFC 7233</a>
     */
    public static final String IF_RANGE = "If-Range";
    /**
     * The HTTP {@code If-Unmodified-Since} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-3.4">Section 3.4 of RFC 7232</a>
     */
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    /**
     * The HTTP {@code Last-Modified} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-2.2">Section 2.2 of RFC 7232</a>
     */
    public static final String LAST_MODIFIED = "Last-Modified";
    /**
     * The HTTP {@code Link} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc5988">RFC 5988</a>
     */
    public static final String LINK = "Link";
    /**
     * The HTTP {@code Location} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.2">Section 7.1.2 of RFC 7231</a>
     */
    public static final String LOCATION = "Location";
    /**
     * The HTTP {@code Max-Forwards} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.1.2">Section 5.1.2 of RFC 7231</a>
     */
    public static final String MAX_FORWARDS = "Max-Forwards";
    /**
     * The HTTP {@code Origin} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc6454">RFC 6454</a>
     */
    public static final String ORIGIN = "Origin";
    /**
     * The HTTP {@code Pragma} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.4">Section 5.4 of RFC 7234</a>
     */
    public static final String PRAGMA = "Pragma";
    /**
     * The HTTP {@code Proxy-Authenticate} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-4.3">Section 4.3 of RFC 7235</a>
     */
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    /**
     * The HTTP {@code Proxy-Authorization} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-4.4">Section 4.4 of RFC 7235</a>
     */
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    /**
     * The HTTP {@code Range} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-3.1">Section 3.1 of RFC 7233</a>
     */
    public static final String RANGE = "Range";
    /**
     * The HTTP {@code Referer} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.2">Section 5.5.2 of RFC 7231</a>
     */
    public static final String REFERER = "Referer";
    /**
     * The HTTP {@code Retry-After} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.3">Section 7.1.3 of RFC 7231</a>
     */
    public static final String RETRY_AFTER = "Retry-After";
    /**
     * The HTTP {@code Server} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.4.2">Section 7.4.2 of RFC 7231</a>
     */
    public static final String SERVER = "Server";
    /**
     * The HTTP {@code Set-Cookie} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc2109#section-4.2.2">Section 4.2.2 of RFC 2109</a>
     */
    public static final String SET_COOKIE = "Set-Cookie";
    /**
     * The HTTP {@code Set-Cookie2} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc2965">RFC 2965</a>
     */
    public static final String SET_COOKIE2 = "Set-Cookie2";
    /**
     * The HTTP {@code TE} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-4.3">Section 4.3 of RFC 7230</a>
     */
    public static final String TE = "TE";
    /**
     * The HTTP {@code Trailer} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-4.4">Section 4.4 of RFC 7230</a>
     */
    public static final String TRAILER = "Trailer";
    /**
     * The HTTP {@code Transfer-Encoding} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-3.3.1">Section 3.3.1 of RFC 7230</a>
     */
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    /**
     * The HTTP {@code Upgrade} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-6.7">Section 6.7 of RFC 7230</a>
     */
    public static final String UPGRADE = "Upgrade";
    /**
     * The HTTP {@code User-Agent} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.5.3">Section 5.5.3 of RFC 7231</a>
     */
    public static final String USER_AGENT = "User-Agent";
    /**
     * The HTTP {@code Vary} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-7.1.4">Section 7.1.4 of RFC 7231</a>
     */
    public static final String VARY = "Vary";
    /**
     * The HTTP {@code Via} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-5.7.1">Section 5.7.1 of RFC 7230</a>
     */
    public static final String VIA = "Via";
    /**
     * The HTTP {@code Warning} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7234#section-5.5">Section 5.5 of RFC 7234</a>
     */
    public static final String WARNING = "Warning";
    /**
     * The HTTP {@code WWW-Authenticate} header field name.
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-4.1">Section 4.1 of RFC 7235</a>
     */
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";


    final MultiValueMap<String, String> headers;


    public HttpHeaders() {
        this(new LinkedMultiValueMap<>(8));
    }

    public HttpHeaders(MultiValueMap<String, String> headers) {
        Assert.notNull(headers, "MultiValueMap must not be null");
        this.headers = headers;
    }

    public String getFirst(String headerName) {
        return this.headers.getFirst(headerName);
    }

    /**
     * Add the given, single header value under the given name.
     * @param headerName the header name
     * @param headerValue the header value
     * @throws UnsupportedOperationException if adding headers is not supported
     * @see #put(String, List)
     * @see #set(String, String)
     */
    @Override
    public void add(String headerName, String headerValue) {
        this.headers.add(headerName, headerValue);
    }

    @Override
    public void addAll(String key, List<? extends String> values) {
        this.headers.addAll(key, values);
    }

    @Override
    public void addAll(MultiValueMap<String, String> values) {
        this.headers.addAll(values);
    }

    /**
     * Set the given, single header value under the given name.
     * @param headerName the header name
     * @param headerValue the header value
     * @throws UnsupportedOperationException if adding headers is not supported
     * @see #put(String, List)
     * @see #add(String, String)
     */
    @Override
    public void set(String headerName, String headerValue) {
        this.headers.set(headerName, headerValue);
    }

    @Override
    public void setAll(Map<String, String> values) {
        this.headers.setAll(values);
    }

    @Override
    public Map<String, String> toSingleValueMap() {
        return this.headers.toSingleValueMap();
    }


    // Map implementation

    @Override
    public int size() {
        return this.headers.size();
    }

    @Override
    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.headers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.headers.containsValue(value);
    }

    @Override
    public List<String> get(Object key) {
        return this.headers.get(key);
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return this.headers.put(key, value);
    }

    @Override
    public List<String> remove(Object key) {
        return this.headers.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> map) {
        this.headers.putAll(map);
    }

    @Override
    public void clear() {
        this.headers.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.headers.keySet();
    }

    @Override
    public Collection<List<String>> values() {
        return this.headers.values();
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return this.headers.entrySet();
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HttpHeaders)) {
            return false;
        }
        HttpHeaders otherHeaders = (HttpHeaders) other;
        return this.headers.equals(otherHeaders.headers);
    }

    @Override
    public int hashCode() {
        return this.headers.hashCode();
    }

    @Override
    public String toString() {
        return formatHeaders(this.headers);
    }


    public static String formatHeaders(MultiValueMap<String, String> headers) {
        return headers.entrySet().stream()
                .map(entry -> {
                    List<String> values = entry.getValue();
                    return entry.getKey() + ":" + (values.size() == 1 ?
                            "\"" + values.get(0) + "\"" :
                            values.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
                })
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
