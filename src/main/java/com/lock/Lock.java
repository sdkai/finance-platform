package com.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RLock;

public interface Lock{

    /**
     *
     * @param joinPoint
     */
    RLock acquire(ProceedingJoinPoint joinPoint);


    void release(RLock rlock);
}
