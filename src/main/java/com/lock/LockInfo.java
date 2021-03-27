package com.lock;

import com.entity.RlockInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RLock;

public interface LockInfo {

    /**
     * 获取锁信息，返回对应的 Rlock 对象
     * @param joinPoint 参数列表
     * @return
     * @throws InterruptedException
     */
    RlockInfo getLockInfo(ProceedingJoinPoint joinPoint) throws InterruptedException;

    /**
     * 释放锁
     * @param rlock
     */
    void release(RLock rlock);
}
