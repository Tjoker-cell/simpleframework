package org.simpleframework.mvc.annotation;

import org.simpleframework.mvc.type.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *标识controller的方法和请求路径和请求方法的映射问题
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    //请求路径
    String value() default "";
    //请求方法
    RequestMethod method() default RequestMethod.GET ;
}
