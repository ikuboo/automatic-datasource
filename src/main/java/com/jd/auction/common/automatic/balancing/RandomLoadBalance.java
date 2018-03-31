package com.jd.auction.common.automatic.balancing;

import com.jd.auction.common.automatic.datasouce.NamedDataSource;
import com.jd.auction.common.automatic.monitor.DataSourceStateJudge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机策略
 */
public final class RandomLoadBalance implements LoadBalance {
    @Override
    public NamedDataSource getDataSource(final NamedDataSource masterDataSource,final List<NamedDataSource> slaveDataSources) {

        List<NamedDataSource> pool = new ArrayList<>(slaveDataSources.size());
        pool.addAll(slaveDataSources);
        NamedDataSource select;

        while (true){
            select = pool.get(new Random().nextInt(slaveDataSources.size()));
            boolean available = DataSourceStateJudge.isAvailable(select);
            if(available){
                break;
            }else {
                if(pool.size() <= 1){
                    break;
                }else {
                    pool.remove(select);
                }
            }
        }

        return select;
    }
}
