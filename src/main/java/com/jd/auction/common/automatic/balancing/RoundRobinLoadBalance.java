package com.jd.auction.common.automatic.balancing;

import com.jd.auction.common.automatic.datasouce.NamedDataSource;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询策略
 */
public final class RoundRobinLoadBalance implements LoadBalance {
    private static final AtomicInteger loop = new AtomicInteger();
    @Override
    public NamedDataSource getDataSource(final List<NamedDataSource> slaveDataSources) {
        loop.compareAndSet(slaveDataSources.size(), 0);
        return slaveDataSources.get(loop.getAndIncrement() % slaveDataSources.size());
    }
}
