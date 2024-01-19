package org.eintr.springframework.annotation.mvc;


import org.eintr.springframework.annotation.beans.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    String name() default "";

    @AliasFor("path")
    String[] value() default {};

    @AliasFor("value")
    String[] path() default {};


    RequestMethod method() default RequestMethod.GET;

    String[] params() default {};


    String[] headers() default {};

    String[] consumes() default {};


    String[] produces() default {};
}