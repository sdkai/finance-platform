package com.service.impl;

import com.annotation.DistributedLock;
import com.annotation.DistributedLockKey;
import com.service.DistributedService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistributedServiceImpl implements DistributedService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    @DistributedLock
    public String getLock(@DistributedLockKey String key) {

        return null;
    }
}
