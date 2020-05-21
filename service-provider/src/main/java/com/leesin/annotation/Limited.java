package com.leesin.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Limited {

    /**
     * 最大限制数量
     *
     * @return
     */
    int value();
}
