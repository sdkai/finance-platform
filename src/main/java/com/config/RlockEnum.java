package com.config;

public enum RlockEnum {

    REENTRANT_LOCK("REENTRANT_LOCK"),
    IS_NULL("IS_NULL");

    private String lockType;

    RlockEnum(String lockType) {
        this.lockType = lockType;
    }

    public String getLockType() {
        return lockType;
    }

    public static RlockEnum getById(String lockType) {
        for (RlockEnum transactType : values()) {
            if (transactType.getLockType() == lockType) {
                //获取指定的枚举
                return transactType;
            }
        }
        return null;
    }
}
