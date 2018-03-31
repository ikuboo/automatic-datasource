package com.jd.auction.common.automatic.balancing;

import com.jd.auction.common.automatic.datasouce.NamedDataSource;

import java.util.List;

/**
 * 负载均衡
 */
public interface LoadBalance {
    /**
     * 获取数据源
     * @param masterDataSource master数据源
     * @param slaveDataSources slaves 数据源集合
     * @return 选择的数据源
     */
    NamedDataSource getDataSource(final NamedDataSource masterDataSource, final List<NamedDataSource> slaveDataSources);
}
