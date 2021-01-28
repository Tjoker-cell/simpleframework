package org.simpleframework.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
   /**
    * 控制类的执行顺序，值越小优先级越高
    */
   int value();
}
