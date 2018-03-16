package com.jd.auction.common.automatic.datasouce;

import com.google.common.base.Preconditions;
import com.jd.auction.common.automatic.balancing.LoadBalance;
import com.jd.auction.common.automatic.balancing.WeightRobinLoadBalance;
import com.jd.auction.common.automatic.connection.MasterSlaveConnection;
import com.jd.auction.common.automatic.connection.NamedConnection;
import com.jd.auction.common.automatic.constant.LoadBalanceStrategy;
import com.jd.auction.common.automatic.constant.SQLType;
import com.jd.auction.common.automatic.monitor.DataSourceHeartBeat;
import com.jd.auction.common.automatic.monitor.DataSourceState;
import com.jd.auction.common.automatic.monitor.DataSourceStateJudge;
import com.jd.auction.common.automatic.monitor.statuslisten.DefStatusChangeListen;
import com.jd.auction.common.automatic.monitor.statuslisten.StatusChangeListen;
import com.jd.auction.common.automatic.utils.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 自动数据源
 * @author yuanchunsen@jd.com
 */

public class AutoMaticDataSource extends AbstractDataSourceAdapter {


    private NamedDataSource masterDataSource;
    private List<NamedDataSource> slaveDataSources;
    private LoadBalanceStrategy loadBalanceStrategy;
    //重试次数，默认为3
    private Integer retry = 3;
    //心跳间隔秒
    private Integer heartBeatPeriod = 1;
    private StatusChangeListen  statusChangeListen  = new DefStatusChangeListen();


    private static final ThreadLocal<Boolean> USER_MASTER_FLAG = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public NamedDataSource getDataSource(final SQLType sqlType) {

        if(null == slaveDataSources || 0 == slaveDataSources.size()){
            return masterDataSource;
        }

        if (isMasterRoute(sqlType)) {
            USER_MASTER_FLAG.set(true);
            return masterDataSource;
        }

        NamedDataSource selectedSource = loadBalanceStrategy == null ?
                LoadBalanceStrategy.getDefaultLoadBalance().getDataSource(masterDataSource, slaveDataSources) :
                loadBalanceStrategy.getLoadBalance().getDataSource(masterDataSource, slaveDataSources);

        Preconditions.checkArgument(selectedSource != null ,"无可用的slave数据源");
        return selectedSource;
    }

    private boolean isMasterRoute(final SQLType sqlType) {
        return SQLType.DQL != sqlType || USER_MASTER_FLAG.get();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new MasterSlaveConnection(this);
    }

    /**
     * 初始化连接池
     */
    public void init(){
        Preconditions.checkNotNull(masterDataSource,"masterDataSource is null");
        Preconditions.checkNotNull(slaveDataSources,"slaveDataSources is null");

        if(StringUtils.isEmpty(masterDataSource.getName())){
            throw new IllegalArgumentException("masterDataSource.name is empty");
        }

        Preconditions.checkNotNull(masterDataSource.getDataSource(), "masterDataSource.datasouce is null");
        //TODO 校验回头在做

        //初始化数据源状态
        DataSourceStateJudge.init(getAllDataSource(), retry, statusChangeListen);
        DataSourceHeartBeat.init(heartBeatPeriod);

    }

    /**
     * 销毁连接池
     */
    public void destory(){
        USER_MASTER_FLAG.remove();
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

    public void setMasterDataSource(NamedDataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public void setSlaveDataSources(List<NamedDataSource> slaveDataSources) {
        this.slaveDataSources = slaveDataSources;
    }

    public void setLoadBalanceStrategy(LoadBalanceStrategy loadBalanceStrategy) {
        this.loadBalanceStrategy = loadBalanceStrategy;
    }
}
