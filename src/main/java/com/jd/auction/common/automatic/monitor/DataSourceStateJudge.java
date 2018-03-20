package com.jd.auction.common.automatic.monitor;

import com.google.common.base.Preconditions;
import com.jd.auction.common.automatic.connection.NamedConnection;
import com.jd.auction.common.automatic.datasouce.NamedDataSource;
import com.jd.auction.common.automatic.monitor.statuslisten.StatusChangeListen;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 数据源状态判断
 */
public final class DataSourceStateJudge {

    //数据源状态变更监听器
    private static StatusChangeListen statusChangeListen;
    private static final List<String> exceptionNames = new ArrayList<>();


    private DataSourceStateJudge() {
    }

    /**
     * 保存所有数据源的状态
     */
    private static volatile Map<NamedDataSource, DataSourceState> dataSourceStatus = new HashMap<>();

    /**
     * 初始化所有数据源的状态
     * 默认为可用
     */
    public static void init(final List<NamedDataSource> allDataSources, final Integer retry,
                            final StatusChangeListen statusChangeListen,final List<String> exceptionNames) {
        DataSourceStateJudge.statusChangeListen = statusChangeListen;
        for (NamedDataSource namedDataSource : allDataSources) {
            dataSourceStatus.put(namedDataSource, new DataSourceState(retry));
        }
        Preconditions.checkArgument(exceptionNames.size() > 0, "exceptionNames.size mast > 0");
        DataSourceStateJudge.exceptionNames.addAll(exceptionNames);
    }

    /**
     * 分析SQLException
     */
    public static void judgeSQLException(final NamedConnection namedConnection, SQLException e) {
        //com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException
        //com.mysql.jdbc.exceptions.jdbc4.CommunicationsException

        Preconditions.checkNotNull(namedConnection);
        Preconditions.checkNotNull(namedConnection.getNamedDataSource());

        String eName = e.getClass().getName();
        String eCause = e.getCause() == null ? null : e.getCause().getClass().getName();
        //判断抛出的异常是否为连接故障的exception
        boolean flag = false;
        for(String exceptionName : exceptionNames){
            if(exceptionName.equals(eName) || exceptionName.equals(eCause)){
                flag = true;
                break;
            }
        }

        if(!flag){
            return;
        }

        NamedDataSource namedDataSource = namedConnection.getNamedDataSource();
        DataSourceState dataSourceState = dataSourceStatus.get(namedDataSource);
        //修改数据源为不可用的
        boolean b = dataSourceState.changeToUnavailable();
        if (b) {
            statusChangeListen.changeToUnavailable(namedDataSource);
        }
    }


    public static void changeToAvailable(final NamedDataSource namedDataSource) {
        DataSourceState dataSourceState = dataSourceStatus.get(namedDataSource);
        Preconditions.checkNotNull(dataSourceState);

        boolean b = dataSourceState.changeToAvailable();
        if (b) {
            statusChangeListen.changeToAvailable(namedDataSource);
        }


    }


    public static boolean isAvailable(final NamedDataSource namedDataSource) {
        DataSourceState dataSourceState = dataSourceStatus.get(namedDataSource);
        Preconditions.checkNotNull(dataSourceState);


        return dataSourceState.isAvailable();

    }

    //获取不可用的数据源
    public static List<NamedDataSource> getUnAvailableDataSource() {
        List<NamedDataSource> unAvailables = new ArrayList<>();

        for (Map.Entry<NamedDataSource, DataSourceState> entry : dataSourceStatus.entrySet()) {
            DataSourceState dataSourceState = entry.getValue();
            if (!dataSourceState.isAvailable()) {
                unAvailables.add(entry.getKey());
            }
        }

        return unAvailables;
    }
}
