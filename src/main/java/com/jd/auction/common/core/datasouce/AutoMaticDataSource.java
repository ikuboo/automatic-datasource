package com.jd.auction.common.core.datasouce;

import com.google.common.base.Preconditions;
import com.jd.auction.common.core.connection.MasterSlaveConnection;
import com.jd.auction.common.core.constant.SQLType;
import com.jd.auction.common.core.rule.MasterSlaveRule;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class AutoMaticDataSource extends AbstractDataSourceAdapter {
    private static final ThreadLocal<Boolean> DML_FLAG = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    private MasterSlaveRule masterSlaveRule;

    public AutoMaticDataSource(final MasterSlaveRule masterSlaveRule, final Map<String, Object> configMap) throws SQLException {
        super(getAllDataSources(masterSlaveRule.getMasterDataSource(), masterSlaveRule.getSlaveDataSourceMap().values()));
        if (!configMap.isEmpty()) {
            //ConfigMapContext.getInstance().getMasterSlaveConfig().putAll(configMap);
        }
        this.masterSlaveRule = masterSlaveRule;
    }

    private static Collection<DataSource> getAllDataSources(final DataSource masterDataSource, final Collection<DataSource> slaveDataSources) {
        Collection<DataSource> result = new LinkedList<>(slaveDataSources);
        result.add(masterDataSource);
        return result;
    }


    public Map<String, DataSource> getAllDataSources() {
        Map<String, DataSource> result = new HashMap<>(masterSlaveRule.getSlaveDataSourceMap().size() + 1, 1);
        result.put(masterSlaveRule.getMasterDataSourceName(), masterSlaveRule.getMasterDataSource());
        result.putAll(masterSlaveRule.getSlaveDataSourceMap());
        return result;
    }


    public Map<String, DataSource> getMasterDataSource() {
        Map<String, DataSource> result = new HashMap<>(1, 1);
        result.put(masterSlaveRule.getMasterDataSourceName(), masterSlaveRule.getMasterDataSource());
        return result;
    }


    public static void resetDMLFlag() {
        DML_FLAG.remove();
    }


    public NamedDataSource getDataSource(final SQLType sqlType) {
        if (isMasterRoute(sqlType)) {
            DML_FLAG.set(true);
            return new NamedDataSource(masterSlaveRule.getMasterDataSourceName(), masterSlaveRule.getMasterDataSource());
        }
        String selectedSourceName = masterSlaveRule.getStrategy().getDataSource(masterSlaveRule.getName(),
                masterSlaveRule.getMasterDataSourceName(), new ArrayList<>(masterSlaveRule.getSlaveDataSourceMap().keySet()));
        DataSource selectedSource = selectedSourceName.equals(masterSlaveRule.getMasterDataSourceName())
                ? masterSlaveRule.getMasterDataSource() : masterSlaveRule.getSlaveDataSourceMap().get(selectedSourceName);
        Preconditions.checkNotNull(selectedSource, "");
        return new NamedDataSource(selectedSourceName, selectedSource);
    }

    private boolean isMasterRoute(final SQLType sqlType) {
        return SQLType.DQL != sqlType || DML_FLAG.get() ;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return new MasterSlaveConnection(this);
    }
}
