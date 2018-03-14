package com.jd.auction.common.core.rule;

import com.google.common.base.Preconditions;
import com.jd.auction.common.core.api.MasterSlaveLoadBalanceAlgorithm;
import com.jd.auction.common.core.api.MasterSlaveLoadBalanceAlgorithmType;


import javax.sql.DataSource;
import java.util.Map;


public final class MasterSlaveRule {
    
    private final String name;
    
    private final String masterDataSourceName;
    
    private final DataSource masterDataSource;
    
    private final Map<String, DataSource> slaveDataSourceMap;
    
    private final MasterSlaveLoadBalanceAlgorithm strategy;
    
    public MasterSlaveRule(final String name, final String masterDataSourceName,
                           final DataSource masterDataSource, final Map<String, DataSource> slaveDataSourceMap) {
        this(name, masterDataSourceName, masterDataSource, slaveDataSourceMap, null);
    }
    
    public MasterSlaveRule(final String name, final String masterDataSourceName,
                           final DataSource masterDataSource, final Map<String, DataSource> slaveDataSourceMap, final MasterSlaveLoadBalanceAlgorithm strategy) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(masterDataSourceName);
        Preconditions.checkNotNull(masterDataSource);
        Preconditions.checkNotNull(slaveDataSourceMap);
        Preconditions.checkState(!slaveDataSourceMap.isEmpty());
        this.name = name;
        this.masterDataSourceName = masterDataSourceName;
        this.masterDataSource = masterDataSource;
        this.slaveDataSourceMap = slaveDataSourceMap;
        this.strategy = null == strategy ? MasterSlaveLoadBalanceAlgorithmType.getDefaultAlgorithmType().getAlgorithm() : strategy;
    }

    public String getName() {
        return name;
    }

    public String getMasterDataSourceName() {
        return masterDataSourceName;
    }

    public DataSource getMasterDataSource() {
        return masterDataSource;
    }

    public Map<String, DataSource> getSlaveDataSourceMap() {
        return slaveDataSourceMap;
    }

    public MasterSlaveLoadBalanceAlgorithm getStrategy() {
        return strategy;
    }
}
