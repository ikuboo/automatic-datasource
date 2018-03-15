package com.jd.auction.common.automatic.monitor;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据源状态
 */
public class DataSourceState {


    private final Integer retry;
    private final AtomicInteger fails =new AtomicInteger();
    private State state= State.Available;


    public DataSourceState(Integer retry){
        this.retry = retry;
    }

    /**
     * 设置数据源 为不可用
     */
    public synchronized boolean changeToUnavailable() {
        if (state == State.Unavailable) {
            return false;
        }

        if (fails.incrementAndGet() >= retry) {
            state = State.Unavailable;
            return true;
        }

        return false;
    }

    /**
     * 设置数据源为可用
     * @return
     */
    public synchronized boolean changeToAvailable() {
        if (state == State.Available) {
            return false;
        }
        state = State.Available;
        fails.set(0);
        return true;
    }


    public void resetFails(){
        fails.set(0);
    }

    public boolean isAvailable() {
        return state == State.Available;
    }

    /**
     * 数据源状态
     */
    enum State {
        Available, Unavailable
    }

}


