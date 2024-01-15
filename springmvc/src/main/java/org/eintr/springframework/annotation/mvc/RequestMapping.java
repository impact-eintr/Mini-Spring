package org.eintr.springframework.annotation.mvc;


import org.eintr.springframework.annotation.beans.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {




    /**
     * Assign a name to this mapping.
     * <p><b>Supported at the type level as well as at the method level!</b>
     * When used on both levels, a combined name is derived by concatenation
     * with "#" as separator.
     * @see org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
     * @see org.springframework.web.servlet.handler.HandlerMethodMappingNamingStrategy
     */
    String name() default "";

    /**
     * The primary mapping expressed by this annotation.
     * <p>This is an alias for {@link #path}. For example,
     * {@code @RequestMapping("/foo")} is equivalent to
     * {@code @RequestMapping(path="/foo")}.
     * <p><b>Supported at the type level as well as at the method level!</b>
     * When used at the type level, all method-level mappings inherit
     * this primary mapping, narrowing it for a specific handler method.
     * <p><strong>NOTE</strong>: A handler method that is not mapped to any path
     * explicitly is effectively mapped to an empty path.
     */
    @AliasFor("path")
    String[] value() default {};

    /**
     * The path mapping URIs (e.g. {@code "/profile"}).
     * <p>Ant-style path patterns are also supported (e.g. {@code "/profile/**"}).
     * At the method level, relative paths (e.g. {@code "edit"}) are supported
     * within the primary mapping expressed at the type level.
     * Path mapping URIs may contain placeholders (e.g. <code>"/${profile_path}"</code>).
     * <p><b>Supported at the type level as well as at the method level!</b>
     * When used at the type level, all method-level mappings inherit
     * this primary mapping, narrowing it for a specific handler method.
     * <p><strong>NOTE</strong>: A handler method that is not mapped to any path
     * explicitly is effectively mapped to an empty path.
     * @since 4.2
     */
    @AliasFor("value")
    String[] path() default {};


    RequestMethod method() default RequestMethod.GET;

    String[] params() default {};


    String[] headers() default {};

    /**
     * Narrows the primary mapping by media types that can be consumed by the
     * mapped handler. Consists of one or more media types one of which must
     * match to the request {@code Content-Type} header. Examples:
     * <pre class="code">
     * consumes = "text/plain"
     * consumes = {"text/plain", "application/*"}
     * consumes = MediaType.TEXT_PLAIN_VALUE
     * </pre>
     * Expressions can be negated by using the "!" operator, as in
     * "!text/plain", which matches all requests with a {@code Content-Type}
     * other than "text/plain".
     * <p><b>Supported at the type level as well as at the method level!</b>
     * If specified at both levels, the method level consumes condition overrides
     * the type level condition.
     * @see org.springframework.http.MediaType
     * @see javax.servlet.http.HttpServletRequest#getContentType()
     */
    String[] consumes() default {};


    String[] produces() default {};
}