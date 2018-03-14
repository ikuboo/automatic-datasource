package com.jd.auction.common.core.api;

import java.util.List;

public interface MasterSlaveLoadBalanceAlgorithm {

    String getDataSource(String name, String masterDataSourceName, List<String> slaveDataSourceNames);
}
