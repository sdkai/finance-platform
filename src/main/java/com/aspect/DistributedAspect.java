package com.aspect;

import com.lock.Lock;
import com.lock.impl.LockInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Aspect
@Component
public class DistributedAspect {

    @Pointcut(value = "@annotation(com.annotation.DistributedLock)")
    public void distributed() {}

    @Autowired
    private Lock lock;

    /**
     * redisson 锁
     * @return
     */
    @Around("distributed()")
    public Object distributedLock(ProceedingJoinPoint joinPoint) throws Throwable {
        RLock Rlock = lock.acquire(joinPoint);
        try {
            boolean flag = Rlock.tryLock(30, TimeUnit.SECONDS);
            if (flag) {
                return joinPoint.proceed();
            }else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.release(Rlock);
        }
        return null;
    }
}
