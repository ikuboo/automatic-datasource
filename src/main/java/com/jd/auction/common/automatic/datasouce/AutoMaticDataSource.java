package com.jd.auction.common.automatic.datasouce;

import com.google.common.base.Preconditions;
import com.jd.auction.common.automatic.connection.MasterSlaveConnection;
import com.jd.auction.common.automatic.constant.LoadBalanceStrategy;
import com.jd.auction.common.automatic.constant.SQLType;
import com.jd.auction.common.automatic.monitor.DataSourceHeartBeat;
import com.jd.auction.common.automatic.monitor.DataSourceStateJudge;
import com.jd.auction.common.automatic.monitor.statuslisten.DefStatusChangeListen;
import com.jd.auction.common.automatic.monitor.statuslisten.StatusChangeListen;
import com.jd.auction.common.automatic.utils.NamedDatasourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自动数据源
 * @author yuanchunsen@jd.com
 */

public class AutoMaticDataSource extends AbstractDataSourceAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AutoMaticDataSource.class);

    private NamedDataSource masterDataSource;
    private List<NamedDataSource> slaveDataSources;
    private LoadBalanceStrategy loadBalanceStrategy;
    //重试次数，默认为3
    private int retry = 3;
    //心跳间隔秒
    private int heartBeatPeriod = 1;
    //数据源状态变更监听器
    private StatusChangeListen  statusChangeListen  = new DefStatusChangeListen();
    //数据源异常抛出的异常
    private String exceptionString ="com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException," +
            "com.mysql.jdbc.exceptions.jdbc4.CommunicationsException," +
            "com.alibaba.druid.pool.GetConnectionTimeoutException";
    private boolean isInit = false;

    private ReentrantLock lock = new ReentrantLock();


    private static final ThreadLocal<Boolean> USER_MASTER_FLAG = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public NamedDataSource getDataSource(final SQLType sqlType) {

        if (isMasterRoute(sqlType)) {
            USER_MASTER_FLAG.set(true);
            return masterDataSource;
        }

        NamedDataSource selectedSource = loadBalanceStrategy == null ?
                LoadBalanceStrategy.getDefaultLoadBalance().getDataSource(masterDataSource, slaveDataSources) :
                loadBalanceStrategy.getLoadBalance().getDataSource(masterDataSource, slaveDataSources);

        Preconditions.checkArgument(selectedSource != null ,"无可用的slave数据源");
        logger.debug("当前使用数据源:{}", selectedSource.getName());
        return selectedSource;
    }

    private boolean isMasterRoute(final SQLType sqlType) {
        return SQLType.DQL != sqlType || USER_MASTER_FLAG.get();
    }

    @Override
    public Connection getConnection() throws SQLException {
        init();
        return new MasterSlaveConnection(this);
    }

    /**
     * 初始化连接池
     */
    public void init(){
        if(isInit){
            return;
        }

        lock.lock();
        try{
            if(isInit){
                return;
            }

            Preconditions.checkNotNull(masterDataSource,"masterDataSource is null");
            Preconditions.checkArgument(masterDataSource.getName() != null && masterDataSource.getName().length() > 0, "masterDataSource.name is empty");
            Preconditions.checkNotNull(masterDataSource.getDataSource(), "masterDataSource.datasouce is null");

            Preconditions.checkNotNull(slaveDataSources, "slaveDataSources is null");
            Preconditions.checkArgument(slaveDataSources.size() > 0 ,"slaveDataSources mast > 0");

            for(NamedDataSource namedDataSource : slaveDataSources){
                Preconditions.checkArgument(namedDataSource.getName() != null && namedDataSource.getName().length() > 0, "slaveDataSources.name is empty");
                Preconditions.checkNotNull(namedDataSource.getDataSource(), "slaveDataSources.datasource is null");
                Preconditions.checkNotNull(namedDataSource.getWeight(),"slaveDataSources.weigth is null");
                Preconditions.checkArgument(namedDataSource.getWeight() > 0 && namedDataSource.getWeight() <= 100, "slaveDataSources.weigth must > 0 && < 100");
            }

            Preconditions.checkArgument(NamedDatasourceUtils.checkDataSourceName(masterDataSource, slaveDataSources), "has same datasource name");

            Preconditions.checkArgument(retry > 0, "retry must > 0");
            Preconditions.checkArgument(heartBeatPeriod > 0, "heartBeatPeriod must > 0");
            Preconditions.checkArgument(exceptionString != null && exceptionString.length() > 0,"exceptionString is empty");

            //初始化异常类型
            String[] exceptionArrays = exceptionString.split(",");
            List<String> exceptions = new ArrayList<>(exceptionArrays.length);
            for(String exceptionArray : exceptionArrays){
                exceptions.add(exceptionArray);
            }

            //初始化数据源状态
            DataSourceStateJudge.init(getAllDataSource(), retry, statusChangeListen, exceptions);
            //初始化心跳线程池
            DataSourceHeartBeat.init(heartBeatPeriod);
            isInit = true;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 销毁连接池
     */
    public void destory(){
        USER_MASTER_FLAG.remove();
        DataSourceHeartBeat.destory();

    }

    public static void resetMasterFlag() {
        USER_MASTER_FLAG.remove();
    }

    public static void userMaster(){
        USER_MASTER_FLAG.set(true);
    }


    @Override
    protected List<NamedDataSource> getAllDataSource() {
        List<NamedDataSource> allDataSource = new ArrayList<>(slaveDataSources.size() + 1);
        allDataSource.add(masterDataSource);
        allDataSource.addAll(slaveDataSources);
        return allDataSource;
    }



    /*-------------get/set------------ method-----------*/
    public void setMasterDataSource(NamedDataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public void setSlaveDataSources(List<NamedDataSource> slaveDataSources) {
        this.slaveDataSources = slaveDataSources;
    }

    public void setLoadBalanceStrategy(LoadBalanceStrategy loadBalanceStrategy) {
        this.loadBalanceStrategy = loadBalanceStrategy;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public void setHeartBeatPeriod(int heartBeatPeriod) {
        this.heartBeatPeriod = heartBeatPeriod;
    }

    public void setStatusChangeListen(StatusChangeListen statusChangeListen) {
        this.statusChangeListen = statusChangeListen;
    }

    public void setExceptionString(String exceptionString) {
        this.exceptionString = exceptionString;
    }
}
