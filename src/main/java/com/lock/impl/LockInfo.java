package com.lock.impl;

import com.annotation.DistributedLockKey;
import com.lock.Lock;
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
public class LockInfo implements Lock {

    @Autowired
    private static RedissonClient redissonClient;

    /**
     * 获取锁信息
     * @param joinPoint
     * @return 锁实例
     */
    public RLock acquire(ProceedingJoinPoint joinPoint) {
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int i1 = 0; i1 <= i; i1++) {
                if (parameterAnnotations[i][i1] instanceof DistributedLockKey) {
                    // 锁名称
                    return redissonClient.getLock(String.valueOf(args[i]));
                }
            }
        }
        return null;
    }

    @Override
    public void release(RLock rlock) {
        if (Objects.nonNull(rlock) && rlock.isHeldByCurrentThread()) {
            rlock.unlock();
        }
    }
}
