package com.jd.auction.common.automatic.balancing;

import com.google.common.base.Preconditions;
import com.jd.auction.common.automatic.datasouce.NamedDataSource;
import com.jd.auction.common.automatic.monitor.DataSourceStateJudge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询策略
 */
public final class RoundRobinLoadBalance implements LoadBalance {
    private static final Logger logger = LoggerFactory.getLogger(RoundRobinLoadBalance.class);
    private static final AtomicInteger loop = new AtomicInteger();
    @Override
    public NamedDataSource getDataSource(final NamedDataSource masterDataSource, final List<NamedDataSource> slaveDataSources) {
        NamedDataSource selectDataSource = null;

        //循环获取数据源，第一个故障了，就获取第二个
        for(int i = 0; i < slaveDataSources.size(); i++){
            loop.compareAndSet(slaveDataSources.size(), 0);
            selectDataSource = slaveDataSources.get(loop.getAndIncrement() % slaveDataSources.size());
            Preconditions.checkNotNull(selectDataSource);
            boolean available = DataSourceStateJudge.isAvailable(selectDataSource);
             if(available){
               break;
            }
        }

        return selectDataSource;
    }
}
