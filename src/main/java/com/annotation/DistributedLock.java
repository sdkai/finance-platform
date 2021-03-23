package com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface DistributedLock {

    /**
     * 锁类型
     */
    String lockType = "reentrant";

    /**
     * 锁持续时间
     */
    Long   leaseTime = 30000L;

}
