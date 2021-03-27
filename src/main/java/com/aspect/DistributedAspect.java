package com.aspect;

import com.controller.ExceptionResult;
import com.lock.LockInfo;
import com.entity.RlockInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Aspect
@Component
public class DistributedAspect {

    @Pointcut(value = "@annotation(com.annotation.DistributedLock)")
    public void distributed() {}

    @Autowired
    private LockInfo lockInfo;

    /**
     * 获取分布式锁
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("distributed()")
    public Object distributedLock(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取锁信息，返回对应的 Rlock 对象
        RlockInfo Info = lockInfo.getLockInfo(joinPoint);
        RLock Rlock = Info.getR_LOCK();
        try {
            // 最设定的时间内无法抢占锁，直接结尾抛出异常，结束代码。
            if (Rlock.tryLock(Info.getWaitTime(), Info.getLeaseTime(), TimeUnit.MILLISECONDS)) {
                return joinPoint.proceed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lockInfo.release(Rlock);
        }
        return new Exception(ExceptionResult.ACQUIRE_LOCK_FAIL);
    }
}
