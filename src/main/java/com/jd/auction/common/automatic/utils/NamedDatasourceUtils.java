package com.jd.auction.common.automatic.utils;

import com.jd.auction.common.automatic.datasouce.NamedDataSource;

import java.util.List;

/**
 * @author yuanchunsen@jd.com
 *         2018/3/15.
 */
public class NamedDatasourceUtils {
    /**
     * 检查数据源的name是否有重复的
     * @return true：无重复name，false:name重复
     */
    public static boolean checkDataSourceName(final NamedDataSource masterDataSource,final List<NamedDataSource> slaveDataSources){
        for(NamedDataSource namedDataSource:slaveDataSources){
            if(namedDataSource.getName().equals(masterDataSource.getName())){
                return false;
            }
            for(NamedDataSource namedDataSource2:slaveDataSources){
                if(namedDataSource.equals(namedDataSource2)){
                    continue;
                }
                if(namedDataSource.getName().equals(namedDataSource2.getName())){
                    return false;
                }
            }
        }
        return true;
    }
}
