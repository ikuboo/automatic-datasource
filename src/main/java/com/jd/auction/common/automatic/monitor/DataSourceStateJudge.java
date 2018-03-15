package com.jd.auction.common.automatic.monitor;

import com.google.common.base.Preconditions;
import com.jd.auction.common.automatic.datasouce.NamedDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源状态判断
 */
public final class DataSourceStateJudge {

    private DataSourceStateJudge() {
    }

    /**
     * 保存所有数据源的状态
     */
    private static volatile Map<String, DataSourceState> dataSourceStatus = new HashMap<>();

    /**
     * 初始化所有数据源的状态
     */
    public static void init(List<NamedDataSource> allDataSources, Integer retry) {
        for (NamedDataSource namedDataSource : allDataSources) {
            dataSourceStatus.put(namedDataSource.getName(), new DataSourceState(retry));
        }
    }

    /**
     * 分析SQLException
     * true：数据源不可用
     */
    public static boolean judgeSQLException(String dataSourceName, Throwable t) {
        //com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException
        //com.mysql.jdbc.exceptions.jdbc4.CommunicationsException
        DataSourceState dataSourceState = dataSourceStatus.get(dataSourceName);
        Preconditions.checkNotNull(dataSourceState);
        return dataSourceState.changeToUnavailable();
    }


    public static boolean changeToAvailable(String dataSourceName) {
        DataSourceState dataSourceState = dataSourceStatus.get(dataSourceName);
        Preconditions.checkNotNull(dataSourceState);
        return dataSourceState.changeToAvailable();

    }


    public static boolean isAvailable(String dataSourceName) {
        DataSourceState dataSourceState = dataSourceStatus.get(dataSourceName);
        Preconditions.checkNotNull(dataSourceState);
        return dataSourceState.isAvailable();
    }

}
