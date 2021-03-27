package com.annotation;

import com.config.RlockEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface DistributedLock {

    /**
     * 锁类型，默认重入锁
     */
    RlockEnum lockType() default RlockEnum.REENTRANT_LOCK;

    /**
     * 锁持续时间，毫秒
     */
    int leaseTime() default  -1;

    /**
     * 等待获取锁的时间
     */
    int waitTime() default  30000;

}
