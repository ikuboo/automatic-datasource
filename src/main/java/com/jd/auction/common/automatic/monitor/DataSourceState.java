package com.jd.auction.common.automatic.monitor;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 数据源状态
 */
public class DataSourceState {
    //重试次数
    private final Integer retry;
    private final AtomicInteger fails = new AtomicInteger();
    private State state = State.Available;

    //数据源状态变更的读写锁
    private final ReentrantReadWriteLock statusChangeLock = new ReentrantReadWriteLock(false);
    //读锁
    private final ReentrantReadWriteLock.ReadLock readLock = statusChangeLock.readLock();
    //写锁
    private final ReentrantReadWriteLock.WriteLock writeLock = statusChangeLock.writeLock();


    public DataSourceState(Integer retry) {
        this.retry = retry;
    }

    /**
     * 设置数据源 为不可用
     */
    public boolean changeToUnavailable() {
        writeLock.lock();
        try {
            if (state == State.Unavailable) {
                return false;
            }

            if (fails.incrementAndGet() >= retry) {
                state = State.Unavailable;
                return true;
            }
            return false;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 设置数据源为可
     */
    public boolean changeToAvailable() {
        writeLock.lock();
        try {
            if (state == State.Available) {
                return false;
            }
            state = State.Available;
            fails.set(0);
            return true;
        } finally {
            writeLock.unlock();
        }
    }


    public boolean isAvailable() {
        readLock.lock();
        try {
            return state == State.Available;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 数据源状态
     */
    enum State {
        Available, Unavailable
    }

}


