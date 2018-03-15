package com.jd.auction.common.automatic.monitor;

import org.junit.Test;


public class DataSourceHeartBeatTest {
    @Test
    public void init() throws Exception {
        DataSourceHeartBeat.init(10);
        Thread.sleep(100000000000L);
    }

    @Test
    public void destory() throws Exception {
    }
}
