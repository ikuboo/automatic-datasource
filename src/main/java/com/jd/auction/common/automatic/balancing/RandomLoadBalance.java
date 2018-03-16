package com.jd.auction.common.automatic.balancing;

import com.jd.auction.common.automatic.datasouce.NamedDataSource;

import java.util.List;
import java.util.Random;

/**
 * 随机策略
 */
public final class RandomLoadBalance implements LoadBalance {
    @Override
    public NamedDataSource getDataSource(final NamedDataSource masterDataSource,final List<NamedDataSource> slaveDataSources) {
        return slaveDataSources.get(new Random().nextInt(slaveDataSources.size()));
    }
}
