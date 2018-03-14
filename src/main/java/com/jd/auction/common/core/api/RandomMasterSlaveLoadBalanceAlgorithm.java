package com.jd.auction.common.core.api;

import java.util.List;
import java.util.Random;

public final class RandomMasterSlaveLoadBalanceAlgorithm implements MasterSlaveLoadBalanceAlgorithm {
    
    @Override
    public String getDataSource(final String name, final String masterDataSourceName, final List<String> slaveDataSourceNames) {
        return slaveDataSourceNames.get(new Random().nextInt(slaveDataSourceNames.size()));
    }
}
