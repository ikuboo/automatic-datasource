package com.jd.auction.common.automatic.balancing;

import com.google.common.base.Preconditions;
import com.jd.auction.common.automatic.datasouce.NamedDataSource;
import com.jd.auction.common.automatic.monitor.DataSourceStateJudge;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 权重轮询
 * @author yuanchunsen@jd.com
 *         2018/3/15.
 */
public class WeightRobinLoadBalance implements LoadBalance {
    private final List<NamedDataSource> switchNamedDataSources = new ArrayList<>();
    private final AtomicInteger loop = new AtomicInteger(0);
    private final ReentrantLock lock = new ReentrantLock();
    //总权重值
    private Integer totalWeight = 0;
    private boolean isInited;



    @Override
    public NamedDataSource getDataSource(final NamedDataSource masterDataSource,final List<NamedDataSource> slaveDataSources) {
        init(slaveDataSources);
        NamedDataSource selectDataSource = null;
        NamedDataSource previousSelectDataSource = null;
        for(int i = 0; i < totalWeight; i++){
            loop.compareAndSet(switchNamedDataSources.size(), 0);
            selectDataSource = switchNamedDataSources.get(loop.getAndIncrement() % switchNamedDataSources.size());
            if(selectDataSource.equals(previousSelectDataSource)){
                continue;
            }
            boolean available = DataSourceStateJudge.isAvailable(selectDataSource);
            if(available){
                break;
            }else {
                previousSelectDataSource = selectDataSource;
                continue;
            }
        }
        return selectDataSource;
    }

    private void init(final List<NamedDataSource> slaveDataSources){
        if(isInited){
            return ;
        }

        try{
            lock.lock();

            if(isInited){
                return;
            }
            for(NamedDataSource namedDataSource:slaveDataSources){
                Integer weight = namedDataSource.getWeight();
                Preconditions.checkNotNull(weight, namedDataSource.getName()  + ".weight is null");
                Preconditions.checkArgument(weight > 0, namedDataSource.getName()  + ".weight master > 0");
                totalWeight += weight;
                for(int i = 0; i < weight; i++){
                    switchNamedDataSources.add(namedDataSource);
                }
            }
            isInited = true;
        }finally {
            lock.unlock();
        }
    }
}
