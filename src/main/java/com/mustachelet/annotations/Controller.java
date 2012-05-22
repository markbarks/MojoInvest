package com.mustachelet.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is the regex for the path that the mustachelet serves.
 * <p/>
 * User: sam
 * Date: 12/21/10
 * Time: 2:24 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Controller {
    HttpMethod.Type[] value() default HttpMethod.Type.GET;
}
