package com.entity;

import lombok.Data;
import org.redisson.api.RLock;

@Data
public class RlockInfo {

    /**
     * 锁实例
     */
    RLock R_LOCK;

    /**
     * 加锁时间默认为-1，激活守护线程
     */
    int leaseTime = -1;

    /**
     * 等待获取锁的时间
     */
    int waitTime = 30000;
}
