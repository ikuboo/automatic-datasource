package com.jd.auction.common.automatic.balancing;

import com.jd.auction.common.automatic.datasouce.NamedDataSource;

import java.util.List;

/**
 * 负载均衡策略
 */
public interface LoadBalanceStrategy {
    /**
     * 获取数据源
     * @param slaveDataSources slaves 数据源集合
     * @return 选择的数据源
     */
    NamedDataSource getDataSource(final List<NamedDataSource> slaveDataSources);
}
