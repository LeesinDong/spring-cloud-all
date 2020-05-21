package com.leesin.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/21 0021 16:33
 * @modified By:
 */
//当放生熔断的时候就走这个注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Timeouted {

    /**
     * 超时时间
     *
     * @return
     */
    long value();

    /**
     * 时间单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 补偿方法，默认可以为空
     * @return
     */
    String fallback() default "";
}
