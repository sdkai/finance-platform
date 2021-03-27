package com.lock.impl;

import com.annotation.DistributedLock;
import com.annotation.DistributedLockKey;
import com.lock.LockInfo;
import com.entity.RlockInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

@Service
public class LockInfoInfoImpl implements LockInfo {

    private final static String LOCK_NAME = "redis.distributed.lock.name:";

    @Autowired
    private static RedissonClient redissonClient;

    /**
     * 获取锁信息，返回对应的 Rlock 对象
     * @param joinPoint 参数列表
     * @return
     * @throws InterruptedException
     */
    @Override
    public RlockInfo getLockInfo(ProceedingJoinPoint joinPoint) throws InterruptedException {
        // 获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取对应的方法
        Method method = signature.getMethod();
        // 获取方法参数列表里全部的参数注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        // 获取方法参数列表里的全部参数，
        Object[] args = joinPoint.getArgs();
        // 遍历注解列表，这是一个二维数组，一维是参数，二位是注解
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int i1 = 0; i1 <= i; i1++) {
                // 通过 instanceof 判断是否为 DistributedLockKey
                if (parameterAnnotations[i][i1] instanceof DistributedLockKey) {
                    // 拼装锁名称，后期优化为 Redis 中获取 LOCK_NAME
                    String lockName = String.valueOf(LOCK_NAME + args[i]);
                    // 获取方法上的 DistributedLock 注解，里面有分布式锁的类型，以及锁持续的时间。
                    DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
                    // 获取对应的分布式锁对象
                    return getLockIntstrance(lockName, distributedLock);
                }
            }
        }
        return null;
    }

    /**
     * 获取 Rlock 实例，组装 RlockInfo 对象
     * @param lockName 锁名称
     * @param distributedLock 锁信息
     */
    private RlockInfo getLockIntstrance(String lockName, DistributedLock distributedLock) throws InterruptedException {
        // 获取 distributedLock 的预设属性
        int leaseTime = distributedLock.leaseTime();
        int waitTime = distributedLock.waitTime();
        // 获取 rlock 实例
        RLock rLock = redissonClient.getLock(lockName);
        // 组装 RlockInfo 对象，并返回。
        RlockInfo rlockInfo = new RlockInfo();
        rlockInfo.setR_LOCK(rLock);
        rlockInfo.setLeaseTime(leaseTime);
        rlockInfo.setWaitTime(waitTime);
        return rlockInfo;
    }

    /**
     * 释放锁
     * @param rlock
     */
    @Override
    public void release(RLock rlock) {
        // 判断 rlock 是否不为空，rlock 是否为当前线程所持有
        if (Objects.nonNull(rlock) && rlock.isHeldByCurrentThread())
            rlock.unlock();
    }
}
