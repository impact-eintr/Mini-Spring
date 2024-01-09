package org.eintr.springframework.web.servlet.support;

import org.eintr.springframework.http.HttpMethod;
import org.eintr.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class WebContentGenerator {



    public static final String METHOD_GET = "GET";

    public static final String METHOD_HEAD = "HEAD";

    public static final String METHOD_POST = "POST";

    private static final String HEADER_PRAGMA = "Pragma";

    private static final String HEADER_EXPIRES = "Expires";

    protected static final String HEADER_CACHE_CONTROL = "Cache-Control";

    protected Set<String> supportedMethods;
    private String allowHeader;


    public WebContentGenerator() {
        this(true);
    }

    /**
     * Create a new WebContentGenerator.
     * @param restrictDefaultSupportedMethods {@code true} if this
     * generator should support HTTP methods GET, HEAD and POST by default,
     * or {@code false} if it should be unrestricted
     */
    public WebContentGenerator(boolean restrictDefaultSupportedMethods) {
        if (restrictDefaultSupportedMethods) {
            this.supportedMethods = new LinkedHashSet<>(4);
            this.supportedMethods.add(METHOD_GET);
            this.supportedMethods.add(METHOD_HEAD);
            this.supportedMethods.add(METHOD_POST);
        }
        initAllowHeader();
    }


    private void initAllowHeader() {
        Collection<String> allowedMethods;
        if (this.supportedMethods == null) {
            allowedMethods = new ArrayList<>(HttpMethod.values().length - 1);
            for (HttpMethod method : HttpMethod.values()) {
                if (method != HttpMethod.TRACE) {
                    allowedMethods.add(method.name());
                }
            }
        }
        else if (this.supportedMethods.contains(HttpMethod.OPTIONS.name())) {
            allowedMethods = this.supportedMethods;
        }
        else {
            allowedMethods = new ArrayList<>(this.supportedMethods);
            allowedMethods.add(HttpMethod.OPTIONS.name());

        }
        this.allowHeader = StringUtils.collectionToCommaDelimitedString(allowedMethods);
    }

    protected String getAllowHeader() {
        return this.allowHeader;
    }
}
